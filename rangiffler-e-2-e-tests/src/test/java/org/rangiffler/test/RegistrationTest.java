package org.rangiffler.test;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.jupiter.annotation.GenerateUser;
import org.rangiffler.jupiter.annotation.User;
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.MainPage;
import org.rangiffler.page.WelcomePage;

import static org.rangiffler.jupiter.extension.CreateUserExtension.USE.METHOD;
import static org.rangiffler.utils.DataUtils.generateRandomPassword;
import static org.rangiffler.utils.DataUtils.generateRandomUsername;
import static org.rangiffler.utils.ErrorMessage.PASSWORDS_SHOULD_BE_EQUAL;

@Epic("[WEB][rangiffler-frontend]: Registration")
@DisplayName("[WEB][rangiffler-frontend]: Registration")
public class RegistrationTest extends BaseWebTest {

    @Test
    @AllureId("2001")
    @DisplayName("WEB: User should be able successfully register in the system")
    @Tag("WEB")
    void shouldRegisterNewUser() {
        String newUsername = generateRandomUsername();
        String password = generateRandomPassword();
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .waitForPageLoaded()
                .doRegister()
                .waitForPageLoaded()
                .setUsername(newUsername)
                .setPassword(password)
                .setPasswordSubmit(password)
                .successSubmit()
                .fillLoginPage(newUsername, password)
                .submit(new MainPage())
                .waitForPageLoaded();
    }

    @Test
    @AllureId("2002")
    @DisplayName("WEB: Error occurs during registration if user with such username already exists")
    @Tag("WEB")
    @GenerateUser()
    void shouldNotRegisterUserWithExistingUsername(@User(use = METHOD) UserGrpc existingUser) {
        String username = existingUser.getUsername();
        String password = generateRandomPassword();
        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .waitForPageLoaded()
                .doRegister()
                .waitForPageLoaded()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .errorSubmit()
                .checkErrorMessage("Username `" + username + "` already exists");
    }

    @Test
    @AllureId("2003")
    @DisplayName("WEB: Error occurs during registration if password and password confirmation are different")
    @Tag("WEB")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = generateRandomUsername();
        String password = generateRandomPassword();
        String submitPassword = generateRandomPassword();

        Selenide.open(WelcomePage.URL, WelcomePage.class)
                .waitForPageLoaded()
                .doRegister()
                .waitForPageLoaded()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(submitPassword)
                .errorSubmit()
                .checkErrorMessage(PASSWORDS_SHOULD_BE_EQUAL.content);
    }

}
