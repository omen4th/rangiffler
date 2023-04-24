package org.rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.jupiter.annotation.*;
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.MainPage;

import java.util.List;

import static org.rangiffler.jupiter.extension.CreateUserExtension.USE.LOGIN;
import static org.rangiffler.jupiter.extension.CreateUserExtension.USE.METHOD;

@Epic("[WEB][rangiffler-frontend]: Friends")
@DisplayName("[WEB][rangiffler-frontend]: Friends")
public class FriendsTest extends BaseWebTest {

    @Test
    @AllureId("4001")
    @DisplayName("WEB: User should be able to see a counter of their friends")
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
    @DisplayName("WEB: User should be able to remove friend via Friends popup")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(friends = @Friends(count = 3)))
    void shouldRemoveFriendViaFriendsPopup(@User UserGrpc user) {
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
    @DisplayName("WEB: User should be able to remove last friend")
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

    @Test
    @AllureId("4005")
    @DisplayName("WEB: User should be able to accept invitation")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(incomeInvitations = @IncomeInvitations(count = 1)))
    void shouldAcceptInvitation(@User UserGrpc user) {
        UserGrpc userToAccept = user.getInvitationsGrpcList().get(0);
        String usernameToAccept = userToAccept.getUsername();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getNavigationPanel()
                .toPeopleAroundPage()
                .acceptInvitationFromUser(usernameToAccept)
                .checkSussesMessage("User " + usernameToAccept + " added to your friends")
                .checkIconChangedToRemoveFriend(usernameToAccept);

        Selenide.refresh();

        mainPage.getHeader()
                .checkFriendsCountInHeader(1)
                .openFriendsPopup()
                .checkTableContainsFriends(List.of(userToAccept));
    }

    @Test
    @AllureId("4006")
    @DisplayName("WEB: User should be able to decline invitation")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(incomeInvitations = @IncomeInvitations(count = 1)))
    void shouldDeclineInvitation(@User UserGrpc user) {
        String usernameToDecline = user.getInvitationsGrpcList().get(0).getUsername();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getNavigationPanel()
                .toPeopleAroundPage()
                .declineInvitationFromUser(usernameToDecline)
                .checkSussesMessage("You declined invitation from user " + usernameToDecline);

        Selenide.refresh();

        mainPage.getNavigationPanel()
                .toPeopleAroundPage()
                .checkIconChangedToAddFriend(usernameToDecline);
    }

    @Test
    @AllureId("4007")
    @DisplayName("WEB: User should be able to remove friend from People Around page")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(friends = @Friends(count = 1)))
    void shouldRemoveFriendFromPeopleAroundPage(@User UserGrpc user) {
        String usernameToRemove = user.getFriendsGrpcList().get(0).getUsername();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getNavigationPanel()
                .toPeopleAroundPage()
                .removeUserFromFriends(usernameToRemove)
                .checkSussesMessage("You're not friends with user " + usernameToRemove + " anymore");

        Selenide.refresh();

        mainPage.getNavigationPanel()
                .toPeopleAroundPage()
                .checkIconChangedToAddFriend(usernameToRemove);

        mainPage.getHeader()
                .checkFriendsCountInHeader(0);
    }

    @Test
    @AllureId("4008")
    @DisplayName("WEB: User should be able to send an invitation from People Around page")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    @GenerateUser()
    void shouldSendInvitation(@User(use = LOGIN) UserGrpc currentUser, @User(use = METHOD) UserGrpc userToSendInvitation) {
        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getNavigationPanel()
                .toPeopleAroundPage()
                .sendInvitationUser(userToSendInvitation.getUsername())
                .checkSussesMessage("Invitation to user " + userToSendInvitation.getUsername() + " is sent");

        Selenide.refresh();

        mainPage.getNavigationPanel()
                .toPeopleAroundPage()
                .checkIconChangedToInvitationSentTest(userToSendInvitation.getUsername());

        mainPage.getHeader()
                .checkFriendsCountInHeader(0);
    }
}
