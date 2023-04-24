package org.rangiffler.test.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;
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
