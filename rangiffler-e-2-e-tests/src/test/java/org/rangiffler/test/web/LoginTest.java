package org.rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.jupiter.annotation.GenerateUser;
import org.rangiffler.jupiter.annotation.User;
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.LoginPage;
import org.rangiffler.page.MainPage;
import org.rangiffler.page.WelcomePage;

import static org.rangiffler.jupiter.extension.CreateUserExtension.USE.METHOD;
import static org.rangiffler.utils.ErrorMessage.BAD_CREDENTIALS;

@Epic("[WEB][rangiffler-frontend]: Authorization")
@DisplayName("[WEB][rangiffler-frontend]: Authorization")
public class LoginTest extends BaseWebTest {

    @Test
    @AllureId("1001")
    @DisplayName("WEB: The main page should be displayed after a user logs in")
    @Tag("WEB")
    @GenerateUser()
    void mainPageShouldBeDisplayedAfterSuccessLogin(@User(use = METHOD) UserGrpc user) {
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(user.getUsername(), user.getPassword())
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("1002")
    @DisplayName("WEB: When user enters incorrect password, they remain unauthorized")
    @Tag("WEB")
    @GenerateUser()
    void userShouldStayOnLoginPageAfterLoginWithBadPasswordCredentials(@User(use = METHOD) UserGrpc user) {
        LoginPage loginPage = Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage(user.getUsername(), "BAD" + user.getPassword());

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }

    @Test
    @AllureId("1003")
    @DisplayName("WEB: When user enters incorrect login, they remain unauthorized")
    @Tag("WEB")
    @GenerateUser()
    void userShouldStayOnLoginPageAfterLoginWithBadLoginCredentials(@User(use = METHOD) UserGrpc user) {
        LoginPage loginPage = Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage("BAD" + user.getUsername(), user.getPassword());

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }
}
