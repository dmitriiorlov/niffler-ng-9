package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username, String password, String friend, String income, String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345", "alex", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dima", "12345", null, "bart", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("bart", "12345", null, null, "dima"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Map<UserType.Type, StaticUser> staticUsers = new HashMap<>();
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class) && p.getType().isAssignableFrom(StaticUser.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(type -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(getUserQueuesByType(type.value()).poll());
                    }
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    user.ifPresentOrElse(
                            u -> staticUsers.putIfAbsent(type.value(), u),
                            () -> {
                                throw new IllegalStateException("Can`t obtain user for type %s after 30s.".formatted(type));
                            }
                    );
                });
        context.getStore(NAMESPACE).put(context.getUniqueId(), staticUsers);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType.Type, StaticUser> userMap =
                context.getStore(NAMESPACE).remove(context.getUniqueId(), Map.class);

        if (userMap != null) {
            for (Map.Entry<UserType.Type, StaticUser> entry : userMap.entrySet()) {
                getUserQueuesByType(entry.getKey()).add(entry.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        UserType.Type userType = parameterContext.getParameter().getAnnotation(UserType.class).value();
        Map<UserType.Type, StaticUser> userMap =
                extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
        return Optional.ofNullable(userMap.get(userType)).orElseThrow();
    }

    private Queue<StaticUser> getUserQueuesByType(UserType.Type userType) {
        return switch (userType) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }
}
