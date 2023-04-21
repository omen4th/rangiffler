package org.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class WelcomePage extends BasePage<WelcomePage> {

    public static final String URL = CFG.frontUrl();

    private final SelenideElement loginButton = $("a[href*='redirect']");
    private final SelenideElement registerButton = $("a[href*='register']");

    @Step("Redirect to the login page")
    public LoginPage doLogin() {
        loginButton.click();
        return new LoginPage();
    }

    @Step("Redirect to the register page")
    public RegisterPage doRegister() {
        registerButton.click();
        return new RegisterPage();
    }

    @Step("Check that the page is loaded")
    @Override
    public WelcomePage waitForPageLoaded() {

        return this;
    }
}