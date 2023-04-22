package org.rangiffler.api;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;
import org.grpc.rangiffler.grpc.username.UsernameRequest;
import org.grpc.rangiffler.grpc.users.InvitationRequest;
import org.grpc.rangiffler.grpc.users.RangifflerUserServiceGrpc;
import org.grpc.rangiffler.grpc.users.UserRequest;
import org.rangiffler.model.UserGrpc;

public class RangifflerUsersGrpcClient extends GrpcClient {
    private static final Channel CHANNEL = ManagedChannelBuilder
            .forAddress(CFG.usersGrpcAddress(), CFG.usersGrpcPort())
            .intercept(new AllureGrpc())
            .usePlaintext()
            .build();

    private final RangifflerUserServiceGrpc.RangifflerUserServiceBlockingStub rangifflerUserServiceStub =
            RangifflerUserServiceGrpc.newBlockingStub(CHANNEL);

    @Step("Send request GetCurrentUser to rangiffler-users")
    public UserGrpc getCurrentUser(String username) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .getCurrentUser(UsernameRequest.newBuilder().setUsername(username).build())
                .getUser()
        );
    }

    @Step("Send request GetCurrentUser to rangiffler-users")
    public UserGrpc updateCurrentUser(UserGrpc user) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .updateCurrentUser(UserRequest.newBuilder().setUser(UserGrpc.toGrpcMessage(user)).build())
                .getUser()
        );
    }

    @Step("Send request SendInvitation to rangiffler-users")
    public UserGrpc sendInvitation(String username, UserGrpc friend) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .sendInvitation(InvitationRequest.newBuilder()
                        .setUsername(username)
                        .setFriend(UserGrpc.toGrpcMessage(friend))
                        .build())
                .getUser()
        );
    }

    @Step("Send request AcceptInvitation to rangiffler-users")
    public UserGrpc acceptInvitation(String username, UserGrpc friend) {
        return UserGrpc.fromGrpcMessage(rangifflerUserServiceStub
                .acceptInvitation(InvitationRequest.newBuilder()
                        .setUsername(username)
                        .setFriend(UserGrpc.toGrpcMessage(friend))
                        .build())
                .getUser()
        );
    }
}
