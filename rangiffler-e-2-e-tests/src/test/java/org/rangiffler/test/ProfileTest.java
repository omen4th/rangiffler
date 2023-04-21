package org.rangiffler.test;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.jupiter.annotation.ApiLogin;
import org.rangiffler.jupiter.annotation.GenerateUser;
import org.rangiffler.jupiter.annotation.User;
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.WelcomePage;

import static com.codeborne.selenide.Selenide.sleep;

@Epic("[WEB][rangiffler-frontend]: Profile")
@DisplayName("[WEB][rangiffler-frontend]: Profile")
public class ProfileTest extends BaseWebTest {

    @Test
    @AllureId("3001")
    @DisplayName("WEB: User can edit all fields in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    void shouldUpdateProfileWithAllFieldsSet(@User UserGrpc user) {

        String username = user.getUsername();
        Selenide.open(WelcomePage.URL, WelcomePage.class);

        System.out.println(username);
        sleep(10000);

    }
}
