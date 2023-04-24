package org.rangiffler.test.grpc;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.rangiffler.api.RangifflerUsersGrpcClient;
import org.rangiffler.jupiter.annotation.*;
import org.rangiffler.model.UserGrpc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.rangiffler.jupiter.extension.CreateUserExtension.USE.METHOD;
import static org.rangiffler.model.FriendStatus.FRIEND;
import static org.rangiffler.utils.DataUtils.generateRandomFirstname;
import static org.rangiffler.utils.DataUtils.generateRandomLastname;
import static org.rangiffler.utils.PhotoUtils.getPhotoByteFromClasspath;

@Epic("[gRPC][rangiffler-users]: Users")
@DisplayName("[gRPC][rangiffler-users]: Users")
public class RangifflerUsersTest extends BaseGRPCTest {

    static final RangifflerUsersGrpcClient usersClient = new RangifflerUsersGrpcClient();
    private static final String ID_REGEXP = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Test
    @AllureId("1201")
    @DisplayName("gRPC: Service rangiffler-users should return not empty users list")
    @GenerateUsers({
            @GenerateUser,
            @GenerateUser
    })
    @Tag("gRPC")
    void getAllUsersTest(@User(use = METHOD) UserGrpc[] users) {
        String currentUsername = users[0].getUsername();
        List<UserGrpc> allUsers = usersClient.getAllUsers(currentUsername);

        step("Check that all users list is not empty", () ->
                assertFalse(allUsers.isEmpty())
        );
    }

    @Test
    @AllureId("1202")
    @DisplayName("gRPC: Service rangiffler-users doesn't return the current user in the users list")
    @GenerateUsers({
            @GenerateUser,
            @GenerateUser
    })
    @Tag("gRPC")
    void getAllUsersForCurrentUserTest(@User(use = METHOD) UserGrpc[] users) {
        String currentUsername = users[0].getUsername();
        List<UserGrpc> allUsers = usersClient.getAllUsers(currentUsername);

        step("Check that all users list doesn't contain the current user", () ->
                assertFalse(allUsers.stream()
                        .map(UserGrpc::getUsername)
                        .toList()
                        .contains(currentUsername))
        );
    }

    @Test
    @AllureId("1203")
    @DisplayName("gRPC: Service rangiffler-users should return all user's info")
    @GenerateUser(
            username = "darth.vader",
            firstname = "Vader",
            lastname = "Darth",
            avatarPath = "img/avatar/dartWader.jpg"
    )
    @Tag("gRPC")
    void getCurrentUserTest(@User(use = METHOD) UserGrpc user) {
        UserGrpc actualUser = usersClient.getCurrentUser(user.getUsername());

        step("Check that response contains ID (GUID)", () ->
                assertTrue(actualUser.getId().toString().matches(ID_REGEXP))
        );

        step("Check that response contains correct username", () ->
                assertEquals(user.getUsername(), actualUser.getUsername())
        );

        step("Check that response contains correct firstname", () ->
                assertEquals(user.getFirstname(), actualUser.getFirstname())
        );

        step("Check that response contains correct lastname", () ->
                assertEquals(user.getLastname(), actualUser.getLastname())
        );

        step("Check that response contains correct avatar", () ->
                assertEquals(new String(user.getAvatar(), StandardCharsets.UTF_8), new String(actualUser.getAvatar(), StandardCharsets.UTF_8))
        );
    }

    @Test
    @AllureId("1204")
    @DisplayName("gRPC: Service rangiffler-users should return default data for a new user if the username doesn't exist")
    @Tag("gRPC")
    @GenerateUser()
    void getNewCurrentUserTest(@User(use = METHOD) UserGrpc user) {
        UserGrpc actualUser = usersClient.getCurrentUser(user.getUsername());

        step("Check that response contains ID (GUID)", () ->
                assertTrue(actualUser.getId().toString().matches(ID_REGEXP))
        );

        step("Check that response contains correct username", () ->
                assertEquals(user.getUsername(), actualUser.getUsername())
        );

        step("Check that response contains empty firstname", () ->
                assertEquals(user.getFirstname(), actualUser.getFirstname())
        );

        step("Check that response contains empty lastname", () ->
                assertEquals(user.getLastname(), actualUser.getLastname())
        );

        step("Check that response contains empty avatar", () ->
                assertEquals(new String(user.getAvatar(), StandardCharsets.UTF_8), new String(actualUser.getAvatar(), StandardCharsets.UTF_8))
        );
    }

    @Test
    @AllureId("1205")
    @DisplayName("gRPC: Service rangiffler-users should persist updated user data")
    @Tag("gRPC")
    @GenerateUser()
    void updateCurrentUserTest(@User(use = METHOD) UserGrpc user) {
        UserGrpc userDataToUpdate = new UserGrpc().toBuilder()
                .username(user.getUsername())
                .firstname(generateRandomFirstname())
                .lastname(generateRandomLastname())
                .avatar(getPhotoByteFromClasspath("img/avatar/wookiee.png"))
                .build();

        UserGrpc actualUser = usersClient.updateCurrentUser(userDataToUpdate);

        step("Check that response contains ID (GUID)", () ->
                assertTrue(actualUser.getId().toString().matches(ID_REGEXP))
        );

        step("Check that response contains correct username", () ->
                assertEquals(userDataToUpdate.getUsername(), actualUser.getUsername())
        );

        step("Check that response contains updated firstname", () ->
                assertEquals(userDataToUpdate.getFirstname(), actualUser.getFirstname())
        );

        step("Check that response contains updated lastname", () ->
                assertEquals(userDataToUpdate.getLastname(), actualUser.getLastname())
        );

        step("Check that response contains updated avatar", () ->
                assertEquals(new String(userDataToUpdate.getAvatar(), StandardCharsets.UTF_8), new String(actualUser.getAvatar(), StandardCharsets.UTF_8))
        );
    }

    @Test
    @AllureId("1206")
    @DisplayName("gRPC: Service rangiffler-users should return a list of friends for the user")
    @Tag("gRPC")
    @GenerateUser(
            friends = @Friends(count = 1),
            outcomeInvitations = @OutcomeInvitations(count = 1),
            incomeInvitations = @IncomeInvitations(count = 1)
    )
    void getFriendsTest(@User(use = METHOD) UserGrpc user) {
        List<UserGrpc> friends = usersClient.getFriends(user.getUsername());
        UserGrpc friend = user.getFriendsGrpcList().get(0);
        UserGrpc friendInResponse = friends.get(0);

        step("Check that all friends list contains expected users", () ->
                assertEquals(1, friends.size())
        );

        step("Check the friend in the response", () -> {
            assertTrue(friend.getId().toString().matches(ID_REGEXP));
            assertEquals(friend.getUsername(), friendInResponse.getUsername());
            assertEquals(FRIEND, friendInResponse.getFriendStatus());
            assertEquals(friend.getLastname(), friendInResponse.getLastname());
            assertEquals(friend.getFirstname(), friendInResponse.getFirstname());
            assertEquals(new String(friend.getAvatar(), StandardCharsets.UTF_8), new String(friendInResponse.getAvatar(), StandardCharsets.UTF_8));
        });

    }
}
