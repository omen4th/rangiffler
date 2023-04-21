package org.rangiffler.test;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.page.LoginPage;
import org.rangiffler.page.MainPage;
import org.rangiffler.page.WelcomePage;

import static org.rangiffler.utils.ErrorMessage.BAD_CREDENTIALS;

@Epic("[WEB][rangiffler-frontend]: Authorization")
@DisplayName("[WEB][rangiffler-frontend]: Authorization")
public class LoginTest extends BaseWebTest {

    @Test
    @AllureId("1001")
    @DisplayName("WEB: The main page should be displayed after a user logs in")
    @Tag("WEB")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage("Kate", "pass")
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("1002")
    @DisplayName("WEB: When user enters incorrect password, they remain unauthorized")
    @Tag("WEB")
    void userShouldStayOnLoginPageAfterLoginWithBadPasswordCredentials() {
        LoginPage loginPage = Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage("Kate", "BAD pass");

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }

    @Test
    @AllureId("1003")
    @DisplayName("WEB: When user enters incorrect login, they remain unauthorized")
    @Tag("WEB")
    void userShouldStayOnLoginPageAfterLoginWithBadLoginCredentials() {
        LoginPage loginPage = Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage("KateBad", "pass");

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }
}
