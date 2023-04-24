package org.rangiffler.api;

import io.qameta.allure.Step;
import org.grpc.rangiffler.grpc.username.UsernameRequest;
import org.grpc.rangiffler.grpc.users.InvitationRequest;
import org.grpc.rangiffler.grpc.users.RangifflerUserServiceGrpc;
import org.grpc.rangiffler.grpc.users.UserRequest;
import org.rangiffler.model.UserGrpc;

import java.util.List;

public class RangifflerUsersGrpcClient extends GrpcClient {

    public RangifflerUsersGrpcClient() {
        super(CFG.usersGrpcAddress(), CFG.usersGrpcPort());
    }

    private final RangifflerUserServiceGrpc.RangifflerUserServiceBlockingStub rangifflerUserServiceStub =
            RangifflerUserServiceGrpc.newBlockingStub(CHANNEL);

    @Step("Request GetCurrentUser to rangiffler-users")
    public UserGrpc getCurrentUser(String username) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .getCurrentUser(UsernameRequest.newBuilder().setUsername(username).build())
                .getUser()
        );
    }

    @Step("Request GetCurrentUser to rangiffler-users")
    public UserGrpc updateCurrentUser(UserGrpc user) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .updateCurrentUser(UserRequest.newBuilder().setUser(UserGrpc.toGrpcMessage(user)).build())
                .getUser()
        );
    }

    @Step("Request SendInvitation to rangiffler-users")
    public UserGrpc sendInvitation(String username, UserGrpc friend) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .sendInvitation(InvitationRequest.newBuilder()
                        .setUsername(username)
                        .setFriend(UserGrpc.toGrpcMessage(friend))
                        .build())
                .getUser()
        );
    }

    @Step("Request AcceptInvitation to rangiffler-users")
    public UserGrpc acceptInvitation(String username, UserGrpc friend) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .acceptInvitation(InvitationRequest.newBuilder()
                        .setUsername(username)
                        .setFriend(UserGrpc.toGrpcMessage(friend))
                        .build())
                .getUser()
        );
    }

    @Step("Request GetAllUsers to rangiffler-users")
    public List<UserGrpc> getAllUsers(String username) {
        return rangifflerUserServiceStub
                .getAllUsers(UsernameRequest.newBuilder().setUsername(username).build())
                .getUsersList()
                .stream().map(UserGrpc::fromGrpcMessage)
                .toList();
    }


    @Step("Request GetFriends to rangiffler-users")
    public List<UserGrpc> getFriends(String username) {
        return rangifflerUserServiceStub
                .getFriends(UsernameRequest.newBuilder().setUsername(username).build())
                .getUsersList()
                .stream().map(UserGrpc::fromGrpcMessage)
                .toList();
    }


}
