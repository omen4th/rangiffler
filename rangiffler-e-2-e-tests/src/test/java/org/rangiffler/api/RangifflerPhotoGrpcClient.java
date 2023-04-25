package org.rangiffler.api;

import io.qameta.allure.Step;
import org.grpc.rangiffler.grpc.photo.PhotoIdRequest;
import org.grpc.rangiffler.grpc.photo.PhotoRequest;
import org.grpc.rangiffler.grpc.photo.RangifflerPhotoServiceGrpc;
import org.grpc.rangiffler.grpc.username.UsernameRequest;
import org.rangiffler.model.PhotoGrpc;

import java.util.List;
import java.util.UUID;

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

    @Step("Request GetAllUserPhotos to rangiffler-photo")
    public List<PhotoGrpc> getAllUserPhotos(String username) {
        return rangifflerPhotoServiceStub
                .getAllUserPhotos(UsernameRequest.newBuilder().setUsername(username).build())
                .getPhotosList()
                .stream().map(PhotoGrpc::fromGrpcMessage)
                .toList();
    }

    @Step("Request EditUserPhoto to rangiffler-photo")
    public PhotoGrpc editUserPhoto(PhotoGrpc photoGrpc) {
        return PhotoGrpc.fromGrpcMessage(rangifflerPhotoServiceStub
                .editUserPhoto(PhotoRequest.newBuilder().setPhoto(PhotoGrpc.toGrpcMessage(photoGrpc)).build())
                .getPhoto()
        );
    }

    @Step("Request DeleteUserPhoto to rangiffler-photo")
    public void deletePhoto(UUID photoId) {
        rangifflerPhotoServiceStub.deleteUserPhoto(PhotoIdRequest.newBuilder().setId(photoId.toString()).build());
    }

    @Step("Request GetAllFriendsPhotos to rangiffler-photo")
    public List<PhotoGrpc> getAllFriendsPhotos(String username) {
        return rangifflerPhotoServiceStub
                .getAllFriendsPhotos(UsernameRequest.newBuilder().setUsername(username).build())
                .getPhotosList()
                .stream().map(PhotoGrpc::fromGrpcMessage)
                .toList();
    }
}
