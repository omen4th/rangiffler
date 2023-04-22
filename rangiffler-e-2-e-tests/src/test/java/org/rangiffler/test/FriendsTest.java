package org.rangiffler.test;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.jupiter.annotation.ApiLogin;
import org.rangiffler.jupiter.annotation.Friends;
import org.rangiffler.jupiter.annotation.GenerateUser;
import org.rangiffler.jupiter.annotation.User;
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.MainPage;

import java.util.List;

@Epic("[WEB][niffler-frontend]: Friends")
@DisplayName("[WEB][niffler-frontend]: Friends")
public class FriendsTest extends BaseWebTest {

    @Test
    @AllureId("4001")
    @DisplayName("WEB: User should be able to see a list of their friends")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(friends = @Friends(count = 2)))
    void shouldViewExistingFriendsCountInHeader(@User UserGrpc user) {
        Selenide.open(MainPage.URL, MainPage.class)
                .getHeader()
                .checkFriendsCountInHeader(user.getFriendsGrpcList().size());
    }

    @Test
    @AllureId("4002")
    @DisplayName("WEB: User should be able to see a list of their friends")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(friends = @Friends(count = 2)))
    void shouldViewExistingFriendsInTable(@User UserGrpc user) {
        List<UserGrpc> friends = user.getFriendsGrpcList();

        Selenide.open(MainPage.URL, MainPage.class)
                .getHeader()
                .openFriendsPopup()
                .checkTableContainsFriends(friends);
    }

    @Test
    @AllureId("4003")
    @DisplayName("WEB: User should be able to remove friend")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(friends = @Friends(count = 3)))
    void shouldRemoveFriend(@User UserGrpc user) {
        List<UserGrpc> friends = user.getFriendsGrpcList();
        UserGrpc friendToRemove = friends.remove(0);
        String usernameToRemove = friendToRemove.getUsername();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openFriendsPopup()
                .removeFriend(usernameToRemove);

        Selenide.refresh();

        mainPage.getHeader()
                .checkFriendsCountInHeader(friends.size())
                .openFriendsPopup()
                .checkTableContainsFriends(friends);
    }

    @Test
    @AllureId("4004")
    @DisplayName("WEB: User should be able to remove friend")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(friends = @Friends(count = 1)))
    void shouldRemoveLastFriend(@User UserGrpc user) {
        List<UserGrpc> friends = user.getFriendsGrpcList();
        UserGrpc friendToRemove = friends.remove(0);
        String usernameToRemove = friendToRemove.getUsername();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openFriendsPopup()
                .removeFriend(usernameToRemove);

        Selenide.refresh();

        mainPage.getHeader()
                .checkFriendsCountInHeader(friends.size())
                .openFriendsPopup()
                .checkTableNotContainsFriends();
    }
}
