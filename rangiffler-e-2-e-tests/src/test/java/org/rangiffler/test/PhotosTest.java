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
import org.rangiffler.page.MainPage;

import static org.rangiffler.model.Country.AUSTRALIA;
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

        MainPage mainPage = Selenide.open(MainPage.URL, MainPage.class);
        mainPage.getHeader()
                .openPopupToAddPhoto()
                .addPhoto(photoPath)
                .setCountry(AUSTRALIA)
                .setDescription(generateRandomDescription())
                .checkPhotoAdded(photoPath)
                .savePhoto();

        mainPage.getHeader()
                .checkPhotosCountInHeader(1)
                .checkCountrisCountInHeader(1);
    }
}
