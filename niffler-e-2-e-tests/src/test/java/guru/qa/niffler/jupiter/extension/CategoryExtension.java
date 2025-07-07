package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.util.RandomDataUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Objects;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class CategoryExtension implements ParameterResolver, BeforeTestExecutionCallback, AfterTestExecutionCallback {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                userAnnotation -> {
                    if (userAnnotation.categories().length > 0) {
                        Category categoryAnnotation = userAnnotation.categories()[0];
                        CategoryEntity ce = spendDbClient.createCategory(new CategoryJson(
                                null,
                                RandomDataUtils.randomCategoryName(),
                                userAnnotation.username(),
                                categoryAnnotation.archived()
                        ));

                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                CategoryJson.fromEntity(ce)
                        );
                    }
                }
        );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson certainTestCategory = context.getStore(NAMESPACE)
                .get(context.getUniqueId(), CategoryJson.class);

        if (Objects.nonNull(certainTestCategory) && !certainTestCategory.archived()) {
            spendDbClient.deleteCategory(CategoryEntity.fromJson(certainTestCategory));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }


    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
      return createdCategory();
    }

    public static CategoryJson createdCategory() {
      final ExtensionContext methodContext = context();
      return methodContext.getStore(NAMESPACE)
        .get(methodContext.getUniqueId(), CategoryJson.class);
    }
}
