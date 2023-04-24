package org.rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.jupiter.annotation.ApiLogin;
import org.rangiffler.jupiter.annotation.GeneratePhoto;
import org.rangiffler.jupiter.annotation.GenerateUser;
import org.rangiffler.jupiter.annotation.User;
import org.rangiffler.model.PhotoGrpc;
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.MainPage;

import java.util.List;

import static org.rangiffler.model.Country.*;
import static org.rangiffler.utils.DataUtils.generateRandomDescription;

@Epic("[WEB][rangiffler-frontend]: Photos")
@DisplayName("[WEB][rangiffler-frontend]: Photos")
public class PhotosTest extends BaseWebTest {

    @Test
    @AllureId("5001")
    @DisplayName("WEB: User should be able add a photo")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    void shouldAddPhoto(@User UserGrpc user) {
        String photoPath = "img/photo/SW1.jpeg";
        String photoDescription = generateRandomDescription();
        String country = AUSTRALIA.toString();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openPopupToAddPhoto()
                .addPhoto(photoPath)
                .setCountry(country)
                .setDescription(photoDescription)
                .checkPhotoAdded(photoPath)
                .savePhoto();

        Selenide.refresh();

        mainPage.getHeader()
                .checkPhotosCountInHeader(1)
                .checkCountriesCountInHeader(1);

        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPath)
                .checkPhotoAdded(photoPath)
                .checkCountry(country)
                .checkDescription(photoDescription);
    }

    @Test
    @AllureId("5002")
    @DisplayName("WEB: User should be able add a photo without setting information about it")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    void shouldAddPhotoWithoutInfo(@User UserGrpc user) {
        String photoPath = "img/photo/SW2.jpeg";
        String defaultCountry = FIJI.toString();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openPopupToAddPhoto()
                .addPhoto(photoPath)
                .checkPhotoAdded(photoPath)
                .savePhoto();

        Selenide.refresh();

        mainPage.getHeader()
                .checkPhotosCountInHeader(1)
                .checkCountriesCountInHeader(1);

        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPath)
                .checkPhotoAdded(photoPath)
                .checkCountry(defaultCountry)
                .checkEmptyDescription();
    }

    @Test
    @AllureId("5003")
    @DisplayName("WEB: User should be able edit a photo info")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(
            photos = @GeneratePhoto(photoPath = "img/photo/SW2.jpeg", country = CANADA, description = "I've got a bad feeling about this.")
    ))
    void shouldRemovePhotoInfo(@User UserGrpc user) {
        String photoPath = user.getPhotosGrpcList().get(0).getPhotoPath();
        String newPhotoDescription = generateRandomDescription();
        String newCountryName = FINLAND.toString();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPath)
                .changePopupToEditMode()
                .setCountry(newCountryName)
                .setDescription(newPhotoDescription)
                .savePhoto();

        Selenide.refresh();

        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPath)
                .checkPhotoAdded(photoPath)
                .checkCountry(newCountryName)
                .checkDescription(newPhotoDescription);
    }

    @Test
    @AllureId("5004")
    @DisplayName("WEB: User should be able delete a photo description")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(
            photos = @GeneratePhoto(photoPath = "img/photo/SW2.jpeg", country = UNITED_KINGDOM, description = "The Force will be with you. Always.")
    ))
    void shouldRemovePhotoDescription(@User UserGrpc user) {
        String photoPath = user.getPhotosGrpcList().get(0).getPhotoPath();
        String newPhotoDescription = "";

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPath)
                .changePopupToEditMode()
                .setDescription(newPhotoDescription)
                .savePhoto();

        Selenide.refresh();

        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPath)
                .checkPhotoAdded(photoPath)
                .checkEmptyDescription();
    }

    @Test
    @AllureId("5005")
    @DisplayName("WEB: User should be able delete a photo")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(
            photos = {
                    @GeneratePhoto(photoPath = "img/photo/SW4.jpeg", country = UNITED_KINGDOM, description = "Use the Force, Luke."),
                    @GeneratePhoto(photoPath = "img/photo/SW5.jpeg", country = GREENLAND, description = "Remember your focus determines your reality.")
            }
    ))
    void shouldDeletePhoto(@User UserGrpc user) {
        List<PhotoGrpc> photos = user.getPhotosGrpcList();
        PhotoGrpc photoToDelete = photos.remove(0);
        String photoPathToDelete = photoToDelete.getPhotoPath();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPathToDelete)
                .deletePhoto();

        Selenide.refresh();

        mainPage.getTravelsPanel()
                .checkPhotosCount(photos.size());

        mainPage.getHeader()
                .checkPhotosCountInHeader(photos.size())
                .checkCountriesCountInHeader(photos.size());
    }

    @Test
    @AllureId("5006")
    @DisplayName("WEB: User should be able delete the last photo")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(
            photos = @GeneratePhoto(photoPath = "img/photo/SW4.jpeg", country = UNITED_KINGDOM, description = "Use the Force, Luke.")

    ))
    void shouldDeleteLastPhoto(@User UserGrpc user) {
        List<PhotoGrpc> photos = user.getPhotosGrpcList();
        PhotoGrpc photoToDelete = photos.remove(0);
        String photoPathToDelete = photoToDelete.getPhotoPath();

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getTravelsPanel()
                .openPhotoPopup(photoPathToDelete)
                .deletePhoto();

        Selenide.refresh();

        mainPage.getTravelsPanel()
                .checkPhotosCount(photos.size());

        mainPage.getHeader()
                .checkPhotosCountInHeader(photos.size())
                .checkCountriesCountInHeader(photos.size());
    }
}
