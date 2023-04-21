package org.rangiffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;
import org.rangiffler.api.auth.RangifflerAuthClient;
import org.rangiffler.api.auth.context.CookieHolder;
import org.rangiffler.api.auth.context.SessionStorageHolder;
import org.rangiffler.config.Config;
import org.rangiffler.jupiter.annotation.ApiLogin;
import org.rangiffler.jupiter.annotation.GenerateUser;
import org.rangiffler.model.UserGrpc;

import java.util.Objects;

import static org.rangiffler.jupiter.extension.CreateUserExtension.API_LOGIN_USERS_NAMESPACE;


public class ApiAuthExtension implements BeforeEachCallback {

    private final RangifflerAuthClient authClient = new RangifflerAuthClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace AUTH_EXTENSION_NAMESPACE
            = ExtensionContext.Namespace.create(ApiAuthExtension.class);

    @Step("Login to rangiffler using api")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        GenerateUser generateUserAnnotation = apiLoginAnnotation.rangifflerUser();
        if ((!generateUserAnnotation.handleAnnotation() && "".equals(apiLoginAnnotation.username()) && "".equals(apiLoginAnnotation.password()))) {
            throw new IllegalArgumentException("You have to provide in @ApiLogin annotation user by username/password or @GenerateUser");
        }
        String testId = getTestId(context);

        UserGrpc userToLogin;
        if (generateUserAnnotation.handleAnnotation()) {
            userToLogin = context.getStore(API_LOGIN_USERS_NAMESPACE).get(testId, UserGrpc.class);
        } else {
            userToLogin = new UserGrpc();
            userToLogin.setUsername(apiLoginAnnotation.username());
            userToLogin.setPassword(apiLoginAnnotation.password());
        }
        apiLogin(userToLogin.getUsername(), userToLogin.getPassword());
        Selenide.open(CFG.frontUrl());
        com.codeborne.selenide.SessionStorage sessionStorage = Selenide.sessionStorage();
        sessionStorage.setItem("codeChallenge", SessionStorageHolder.getInstance().getCodeChallenge());
        sessionStorage.setItem("id_token", SessionStorageHolder.getInstance().getToken());
        sessionStorage.setItem("codeVerifier", SessionStorageHolder.getInstance().getCodeVerifier());

        WebDriverRunner.getWebDriver().manage()
                .addCookie(new Cookie("JSESSIONID", CookieHolder.getInstance().getCookieValueByPart("JSESSIONID")));
    }

    private void apiLogin(String username, String password) throws Exception {
        authClient.authorize();
        authClient.login(username, password);
        JsonNode token = authClient.getToken();
        SessionStorageHolder.getInstance().addToken(token.get("id_token").asText());
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }
}
