package org.rangiffler.api;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.Step;
import io.qameta.allure.grpc.AllureGrpc;
import org.grpc.rangiffler.grpc.photo.PhotoRequest;
import org.grpc.rangiffler.grpc.photo.RangifflerPhotoServiceGrpc;
import org.rangiffler.model.PhotoGrpc;

public class RangifflerPhotoGrpcClient extends GrpcClient {
    private static final Channel CHANNEL = ManagedChannelBuilder
            .forAddress(CFG.photoGrpcAddress(), CFG.photoGrpcPort())
            .intercept(new AllureGrpc())
            .usePlaintext()
            .build();

    private final RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub rangifflerPhotoServiceStub =
            RangifflerPhotoServiceGrpc.newBlockingStub(CHANNEL);


    @Step("Send request AddUserPhoto to rangiffler-photo")
    public PhotoGrpc addUserPhoto(PhotoGrpc photoGrpc) {
        return PhotoGrpc.fromGrpcMessage(rangifflerPhotoServiceStub
                .addUserPhoto(PhotoRequest.newBuilder().setPhoto(PhotoGrpc.toGrpcMessage(photoGrpc)).build())
                .getPhoto()
        );
    }


}
