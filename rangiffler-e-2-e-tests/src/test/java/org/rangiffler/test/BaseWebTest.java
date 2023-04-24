package org.rangiffler.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Cookie;
import org.rangiffler.api.auth.RangifflerAuthClient;
import org.rangiffler.api.auth.context.CookieHolder;
import org.rangiffler.api.auth.context.SessionStorageHolder;
import org.rangiffler.config.Config;
import org.rangiffler.jupiter.annotation.meta.DBTest;
import org.rangiffler.jupiter.annotation.meta.WebTest;

@WebTest
@DBTest
public class BaseWebTest {

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false)
        );
    }
}
