package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;
import java.util.List;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                userAnnotation -> {
                    if (userAnnotation.spendings().length > 0) {
                        Spending spending = userAnnotation.spendings()[0];
                        CategoryJson category;
                        List<CategoryEntity> userCategories = spendDbClient.findAllCategoriesByUserName(userAnnotation.username());

                        if (userCategories.stream()
                                .map(CategoryEntity::getName)
                                .toList().contains(spending.category())) {
                            CategoryEntity ce = userCategories.stream()
                                    .filter(uc -> uc.getName().equals(spending.category()))
                                    .findFirst().orElseThrow();
                            category = CategoryJson.fromEntity(ce);
                        } else {
                            category = new CategoryJson(
                                    null,
                                    spending.category(),
                                    userAnnotation.username(),
                                    false
                            );
                        }
                        SpendJson spend = spendDbClient.createSpend(new SpendJson(
                                null,
                                new Date(),
                                category,
                                spending.currency(),
                                spending.amount(),
                                spending.description(),
                                userAnnotation.username()
                        ));
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                spend
                        );
                    }
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return createdSpending();
    }

    public static SpendJson createdSpending() {
        final ExtensionContext methodContext = context();
        return methodContext.getStore(NAMESPACE)
                .get(methodContext.getUniqueId(), SpendJson.class);
    }
}
