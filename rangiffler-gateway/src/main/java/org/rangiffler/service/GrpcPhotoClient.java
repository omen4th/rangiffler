package org.rangiffler.service;

import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.grpc.rangiffler.grpc.photo.PhotoIdRequest;
import org.grpc.rangiffler.grpc.photo.PhotoRequest;
import org.grpc.rangiffler.grpc.photo.RangifflerPhotoServiceGrpc;
import org.grpc.rangiffler.grpc.username.UsernameRequest;
import org.rangiffler.model.PhotoJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.rangiffler.model.PhotoJson.toGrpcMessage;

@Component
public class GrpcPhotoClient {
    private static final Logger LOG = LoggerFactory.getLogger(GrpcGeoClient.class);

    @GrpcClient("grpcPhotoClient")
    private RangifflerPhotoServiceGrpc.RangifflerPhotoServiceBlockingStub rangifflerPhotoServiceStub;

    public @Nonnull List<PhotoJson> getAllUserPhotos(String username) {
        try {
            return rangifflerPhotoServiceStub
                    .getAllUserPhotos(UsernameRequest.newBuilder().setUsername(username).build())
                    .getPhotosList()
                    .stream().map(PhotoJson::fromGrpcMessage)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull List<PhotoJson> getAllFriendsPhotos(String username) {
        try {
            return rangifflerPhotoServiceStub
                    .getAllFriendsPhotos(UsernameRequest.newBuilder().setUsername(username).build())
                    .getPhotosList()
                    .stream().map(PhotoJson::fromGrpcMessage)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull PhotoJson addPhoto(PhotoJson photoJson) {
        try {
            return PhotoJson.fromGrpcMessage(rangifflerPhotoServiceStub
                    .addUserPhoto(PhotoRequest.newBuilder().setPhoto(toGrpcMessage(photoJson)).build())
                    .getPhoto()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull PhotoJson editPhoto(PhotoJson photoJson) {
        try {
            return PhotoJson.fromGrpcMessage(rangifflerPhotoServiceStub
                    .addUserPhoto(PhotoRequest.newBuilder().setPhoto(toGrpcMessage(photoJson)).build())
                    .getPhoto()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public void deletePhoto(UUID photoId) {
        rangifflerPhotoServiceStub.deleteUserPhoto(PhotoIdRequest.newBuilder().setId(photoId.toString()).build());
    }

}
