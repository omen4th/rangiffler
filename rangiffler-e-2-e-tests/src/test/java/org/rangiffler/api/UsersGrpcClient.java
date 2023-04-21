package org.rangiffler.api;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;
import org.grpc.rangiffler.grpc.users.RangifflerUserServiceGrpc;
import org.rangiffler.model.UserGrpc;

public class UsersGrpcClient extends GrpcClient {
    private static final Channel CHANNEL = ManagedChannelBuilder
            .forAddress(CFG.usersGrpcAddress(), CFG.usersGrpcPort())
            .intercept(new AllureGrpc())
            .usePlaintext()
            .build();

    private final RangifflerUserServiceGrpc.RangifflerUserServiceBlockingStub rangifflerUserServiceStub =
            RangifflerUserServiceGrpc.newBlockingStub(CHANNEL);

//    @Step("Send request GetCurrentUser to rangiffler-users")
//    public UserGrpc getCurrentUser()
}
