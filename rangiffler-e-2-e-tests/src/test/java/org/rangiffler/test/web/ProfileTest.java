package org.rangiffler.test.web;

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
import org.rangiffler.page.MainPage;

import static org.rangiffler.utils.DataUtils.generateRandomFirstname;
import static org.rangiffler.utils.DataUtils.generateRandomLastname;

@Epic("[WEB][rangiffler-frontend]: Profile")
@DisplayName("[WEB][rangiffler-frontend]: Profile")
public class ProfileTest extends BaseWebTest {

    @Test
    @AllureId("3001")
    @DisplayName("WEB: User should be able fill all fields in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    void shouldFillProfileWithAllFieldsSet(@User UserGrpc user) {
        String newFirstname = generateRandomFirstname();
        String newLastname = generateRandomLastname();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openProfilePopup()
                .setFirstname(newFirstname)
                .setLastname(newLastname)
                .saveProfile();

        Selenide.refresh();

        mainPage.getHeader()
                .openProfilePopup()
                .checkFirstname(newFirstname)
                .checkLastname(newLastname);
    }

    @Test
    @AllureId("3002")
    @DisplayName("WEB: User should be able update all fields in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(firstname = "Test firstname", lastname = "Test lastname"))
    void shouldUpdateProfileWithAllFieldsSet(@User UserGrpc user) {
        String newFirstname = generateRandomFirstname();
        String newLastname = generateRandomLastname();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openProfilePopup()
                .setFirstname(newFirstname)
                .setLastname(newLastname)
                .saveProfile();

        Selenide.refresh();

        mainPage.getHeader()
                .openProfilePopup()
                .checkFirstname(newFirstname)
                .checkLastname(newLastname);
    }

    @Test
    @AllureId("3003")
    @DisplayName("WEB: User should be able add an avatar in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    void shouldAddAvatarIntoProfile(@User UserGrpc user) {
        String avatarPath = "img/avatar/avatar.jpg";

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openProfilePopup()
                .updateAvatar(avatarPath)
                .saveProfile();

        Selenide.refresh();

        mainPage.getHeader()
                .openProfilePopup()
                .checkAvatar(avatarPath);
    }

    @Test
    @AllureId("3004")
    @DisplayName("WEB: User should be able update an avatar in the profile")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(avatarPath = "img/avatar/dartWader.jpg"))
    void shouldUpdateAvatarInProfile(@User UserGrpc user) {
        String avatarPath = "img/avatar/avatar.jpg";

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openProfilePopup()
                .updateAvatar(avatarPath)
                .saveProfile();

        Selenide.refresh();

        mainPage.getHeader()
                .openProfilePopup()
                .checkAvatar(avatarPath);
    }
}
