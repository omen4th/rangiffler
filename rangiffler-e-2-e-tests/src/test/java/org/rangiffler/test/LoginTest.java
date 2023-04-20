package org.rangiffler.test;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.page.LoginPage;
import org.rangiffler.page.MainPage;
import org.rangiffler.page.WelcomePage;

import static org.rangiffler.utils.Error.BAD_CREDENTIALS;

@Epic("[WEB][rangiffler-frontend]: Authorization")
@DisplayName("[WEB][rangiffler-frontend]: Authorization")
public class LoginTest extends BaseWebTest {

    @Test
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
    @DisplayName("WEB: When user enters incorrect login/password, they remain unauthorized")
    @Tag("WEB")
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        LoginPage loginPage = Selenide.open(WelcomePage.URL, WelcomePage.class)
                .doLogin()
                .fillLoginPage("Kate", "BAD pass");

        loginPage.submit(loginPage)
                .checkError(BAD_CREDENTIALS.content);
    }
}
