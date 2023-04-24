package org.rangiffler.api;

import io.qameta.allure.Step;
import org.grpc.rangiffler.grpc.photo.PhotoRequest;
import org.grpc.rangiffler.grpc.photo.RangifflerPhotoServiceGrpc;
import org.rangiffler.model.PhotoGrpc;

public class RangifflerPhotoGrpcClient extends GrpcClient {

    public RangifflerPhotoGrpcClient() {
        super(CFG.photoGrpcAddress(), CFG.photoGrpcPort());
    }

    private final RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub rangifflerPhotoServiceStub =
            RangifflerPhotoServiceGrpc.newBlockingStub(CHANNEL);


    @Step("Request AddUserPhoto to rangiffler-photo")
    public PhotoGrpc addUserPhoto(PhotoGrpc photoGrpc) {
        return PhotoGrpc.fromGrpcMessage(rangifflerPhotoServiceStub
                .addUserPhoto(PhotoRequest.newBuilder().setPhoto(PhotoGrpc.toGrpcMessage(photoGrpc)).build())
                .getPhoto()
        );
    }

}
