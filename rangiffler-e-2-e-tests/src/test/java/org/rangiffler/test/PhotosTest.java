package org.rangiffler.test;

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
import org.rangiffler.model.UserGrpc;
import org.rangiffler.page.MainPage;

import static org.rangiffler.model.Country.*;
import static org.rangiffler.utils.DataUtils.generateRandomDescription;

@Epic("[WEB][rangiffler-frontend]: Travels")
@DisplayName("[WEB][rangiffler-frontend]: Travels")
public class PhotosTest extends BaseWebTest {

    @Test
    @AllureId("5001")
    @DisplayName("WEB: User can add a photo")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser)
    void shouldAddPhoto(@User UserGrpc user) {
        String photoPath = "img/photo/SW1.jpeg";
        String photoDescription = generateRandomDescription();
        String country = AUSTRALIA.name();

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
    @DisplayName("WEB: User can add a photo without setting information about it")
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
    @DisplayName("WEB: User can edit a photo info")
    @Tag("WEB")
    @ApiLogin(rangifflerUser = @GenerateUser(
            photos = @GeneratePhoto(photoPath = "img/photo/SW2.jpeg", country = CANADA, description = "I've got a bad feeling about this.")
    ))
    void shouldEditPhotoInfo(@User UserGrpc user) {
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
}
