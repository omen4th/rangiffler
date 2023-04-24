package org.rangiffler.test.web;

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
import org.rangiffler.model.PhotoGrpc;
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.MainPage;

import java.util.List;

@Epic("[WEB][rangiffler-frontend]: Friends Photos")
@DisplayName("[WEB][rangiffler-frontend]: Friends Photos")
public class FriendsPhotosTest extends BaseWebTest {
    @Test
    @AllureId("6001")
    @DisplayName("WEB: User should be able to see friends' photos")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(friends = @Friends(count = 2, withPhotos = true)))
    void shouldViewFriendsPhotos(@User UserGrpc user) {
        List<PhotoGrpc> friendsPhotos = user.getFriendsGrpcList().stream()
                .flatMap(friend -> friend.getPhotosGrpcList().stream())
                .toList();
        PhotoGrpc photoToCheck = friendsPhotos.get(1);

        Selenide.open(MainPage.URL, MainPage.class)
                .getNavigationPanel()
                .toFriendsTravelsPage()
                .getTravelsPanel()
                .checkPhotosCount(friendsPhotos.size())
                .openPhotoPopup(photoToCheck.getPhotoPath())
                .checkPhotoAdded(photoToCheck.getPhotoPath())
                .checkCountry(photoToCheck.getCountry().getName())
                .checkDescription(photoToCheck.getDescription());
    }
}
