package org.rangiffler.test.grpc;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rangiffler.api.RangifflerPhotoGrpcClient;
import org.rangiffler.jupiter.annotation.Friends;
import org.rangiffler.jupiter.annotation.GeneratePhoto;
import org.rangiffler.jupiter.annotation.GenerateUser;
import org.rangiffler.jupiter.annotation.User;
import org.rangiffler.model.CountryGrpc;
import org.rangiffler.model.PhotoGrpc;
import org.rangiffler.model.UserGrpc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static org.rangiffler.jupiter.extension.CreateUserExtension.USE.METHOD;
import static org.rangiffler.model.Country.*;
import static org.rangiffler.utils.PhotoUtils.getPhotoByteFromClasspath;

@Epic("[gRPC][rangiffler-photo]: Photo")
@DisplayName("[gRPC][rangiffler-photo]: Photo")
public class RangifflerPhotoGrpcTest extends BaseGRPCTest {

    static final RangifflerPhotoGrpcClient photoClient = new RangifflerPhotoGrpcClient();
    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Test
    @AllureId("1301")
    @DisplayName("gRPC: Service rangiffler-photo should return user's photo list")
    @GenerateUser(
            photos = @GeneratePhoto(
                    photoPath = "img/photo/SW10.jpeg",
                    country = MADAGASCAR,
                    description = "I have a bad feeling about this.")
    )
    @Tag("gRPC")
    void getAllUserPhotosTest(@User(use = METHOD) UserGrpc user) {
        List<PhotoGrpc> userPhotos = photoClient.getAllUserPhotos(user.getUsername());
        PhotoGrpc expectedPhoto = user.getPhotosGrpcList().get(0);
        PhotoGrpc actualPhoto = userPhotos.get(0);

        step("Check that all photos list is not empty", () ->
                assertEquals(user.getPhotosGrpcList().size(), userPhotos.size())
        );

        step("Check the photo country in the response", () ->
                assertEquals(expectedPhoto.getCountry().getName(), actualPhoto.getCountry().getName())
        );

        step("Check the photo description in the response", () ->
                assertEquals(expectedPhoto.getDescription(), actualPhoto.getDescription())
        );

        step("Check the photo in the response", () ->
                assertEquals(new String(expectedPhoto.getPhoto(), StandardCharsets.UTF_8), new String(actualPhoto.getPhoto(), StandardCharsets.UTF_8))
        );
    }

    @Test
    @AllureId("1302")
    @DisplayName("gRPC: Service rangiffler-photo should return empty list if the user doesn't have photos")
    @GenerateUser()
    @Tag("gRPC")
    void getAllUserPhotosEmptyListTest(@User(use = METHOD) UserGrpc user) {
        List<PhotoGrpc> userPhotos = photoClient.getAllUserPhotos(user.getUsername());

        step("Check that photos list is empty", () ->
                assertEquals(0, userPhotos.size())
        );
    }

    @ParameterizedTest(name = "With description: {0}")
    @AllureId("1303")
    @DisplayName("gRPC: Service rangiffler-photo should save a new photo")
    @ValueSource(strings = {
            "",
            "In my experience there is no such thing as luck."
    })
    @GenerateUser()
    @Tag("gRPC")
    void addPhotoTest(String photoDescription, @User(use = METHOD) UserGrpc user) {
        PhotoGrpc expectedPhoto = new PhotoGrpc();
        expectedPhoto.setPhoto(getPhotoByteFromClasspath("img/photo/SW9.jpeg"));
        expectedPhoto.setDescription(photoDescription);
        expectedPhoto.setCountry(CountryGrpc.builder().name(NEW_ZEALAND.toString()).code(NEW_ZEALAND.getCode()).build());
        expectedPhoto.setUsername(user.getUsername());

        PhotoGrpc actualPhoto = photoClient.addUserPhoto(expectedPhoto);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(actualPhoto.getId().toString().matches(ID_REGEXP))
        );

        step("Check that response contains correct country", () -> {
            assertEquals(expectedPhoto.getCountry().getName(), actualPhoto.getCountry().getName());
            assertEquals(expectedPhoto.getCountry().getCode(), actualPhoto.getCountry().getCode());
        });

        step("Check that response contains correct photo", () ->
                assertEquals(new String(expectedPhoto.getPhoto(), StandardCharsets.UTF_8), new String(actualPhoto.getPhoto(), StandardCharsets.UTF_8))
        );

        step("Check that response contains correct description", () ->
                assertEquals(expectedPhoto.getDescription(), actualPhoto.getDescription())

        );

        step("Check that response contains correct username", () ->
                assertEquals(expectedPhoto.getUsername(), actualPhoto.getUsername())
        );
    }

    @ParameterizedTest(name = "With description: {0}")
    @AllureId("1304")
    @DisplayName("gRPC: Service rangiffler-photo should persist updated photo")
    @GenerateUser(
            photos = @GeneratePhoto(
                    photoPath = "img/photo/SW8.jpeg",
                    country = CZECH_REPUBLIC,
                    description = "I have a bad feeling about this.")
    )
    @ValueSource(strings = {
            "",
            "This deal is getting worse all the time."
    })
    @Tag("gRPC")
    void editPhotoTest(String photoDescription, @User(use = METHOD) UserGrpc user) {
        PhotoGrpc photoInfoNew = new PhotoGrpc();
        photoInfoNew.setDescription(photoDescription);
        photoInfoNew.setCountry(CountryGrpc.builder().name(AUSTRIA.toString()).code(AUSTRIA.getCode()).build());
        photoInfoNew.setPhoto(user.getPhotosGrpcList().get(0).getPhoto());
        photoInfoNew.setUsername(user.getUsername());
        photoInfoNew.setId(user.getPhotosGrpcList().get(0).getId());

        PhotoGrpc actualPhoto = photoClient.editUserPhoto(photoInfoNew);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(actualPhoto.getId().toString().matches(ID_REGEXP))
        );

        step("Check that response contains correct country", () -> {
            assertEquals(photoInfoNew.getCountry().getName(), actualPhoto.getCountry().getName());
            assertEquals(photoInfoNew.getCountry().getCode(), actualPhoto.getCountry().getCode());
        });

        step("Check that response contains correct photo", () ->
                assertEquals(new String(photoInfoNew.getPhoto(), StandardCharsets.UTF_8), new String(actualPhoto.getPhoto(), StandardCharsets.UTF_8))
        );

        step("Check that response contains correct description", () ->
                assertEquals(photoInfoNew.getDescription(), actualPhoto.getDescription())

        );

        step("Check that response contains correct username", () ->
                assertEquals(photoInfoNew.getUsername(), actualPhoto.getUsername())
        );
    }

    @Test
    @AllureId("1305")
    @DisplayName("gRPC: Service rangiffler-photo should delete photo")
    @GenerateUser(
            photos = {
                    @GeneratePhoto(
                            photoPath = "img/photo/SW7.jpeg",
                            country = COTED_IVOIRE,
                            description = "We seem to be made to suffer. It's our lot in life."),
                    @GeneratePhoto(
                            photoPath = "img/photo/SW6.jpeg",
                            country = GUINEA,
                            description = "R2-D2, you know better than to trust a strange computer!")
            })
    @Tag("gRPC")
    void deletePhotoTest(@User(use = METHOD) UserGrpc user) {
        List<PhotoGrpc> photos = user.getPhotosGrpcList();
        PhotoGrpc photoToDelete = photos.remove(0);

        photoClient.deletePhoto(photoToDelete.getId());

        List<PhotoGrpc> actualUserPhotosList = photoClient.getAllUserPhotos(user.getUsername());

        step("Check that all photos list doesn't contain the deleted photo", () -> {
            assertEquals(photos.size(), actualUserPhotosList.size());
            assertFalse(actualUserPhotosList.stream()
                    .map(PhotoGrpc::getId)
                    .toList()
                    .contains(photoToDelete.getId()));
        });
    }

    @Test
    @AllureId("1306")
    @DisplayName("gRPC: Service rangiffler-photo should return friends' photos")
    @GenerateUser(friends = @Friends(count = 2, withPhotos = true))
    @Tag("gRPC")
    void getAllFriendsPhotosTest(@User(use = METHOD) UserGrpc user) {
        List<PhotoGrpc> expectedFriendsPhotosList = user.getFriendsGrpcList().stream()
                .flatMap(friend -> friend.getPhotosGrpcList().stream())
                .toList();

        List<PhotoGrpc> actualFriendsPhotosList = photoClient.getAllFriendsPhotos(user.getUsername());

        step("Check that all response contain all friends' photos", () -> {
            assertEquals(expectedFriendsPhotosList.size(), actualFriendsPhotosList.size());
            assertTrue(actualFriendsPhotosList.stream()
                    .allMatch(photo -> expectedFriendsPhotosList.stream()
                            .map(PhotoGrpc::getId)
                            .toList()
                            .contains(photo.getId())));
        });
    }
}