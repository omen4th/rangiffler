package org.rangiffler.jupiter.extension;

import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.*;
import org.rangiffler.api.RangifflerAuthClient;
import org.rangiffler.config.Config;
import org.rangiffler.jupiter.annotation.User;
import org.rangiffler.model.UserGrpc;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {


    private final RangifflerAuthClient authClient = new RangifflerAuthClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class);


    @Step("Create user for test")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return (parameterType.isAssignableFrom(UserGrpc.class) || parameterType.isAssignableFrom(UserGrpc[].class))
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        return extensionContext.getStore(API_LOGIN_USERS_NAMESPACE).get(testId);
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }
}
