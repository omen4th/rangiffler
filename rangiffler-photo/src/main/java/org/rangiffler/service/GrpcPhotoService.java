package org.rangiffler.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.grpc.rangiffler.grpc.photo.*;
import org.grpc.rangiffler.grpc.username.UsernameRequest;
import org.rangiffler.data.PhotoEntity;
import org.rangiffler.data.repository.PhotoRepository;
import org.rangiffler.model.CountryGrpcMessage;
import org.rangiffler.model.PhotoGrpcMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.rangiffler.model.PhotoGrpcMessage.*;

@GrpcService
public class GrpcPhotoService extends RangifflerPhotoServiceGrpc.RangifflerPhotoServiceImplBase {
    private final PhotoRepository photoRepository;
    private final GrpcGeoClient geoClient;
    private final GrpcUsersClient usersClient;

    @Autowired
    public GrpcPhotoService(PhotoRepository photoRepository, GrpcGeoClient geoClient, GrpcUsersClient usersClient) {
        this.photoRepository = photoRepository;
        this.geoClient = geoClient;
        this.usersClient = usersClient;
    }

    @Override
    public void getAllUserPhotos(UsernameRequest request, StreamObserver<AllPhotosResponse> responseObserver) {
        String username = request.getUsername();

        List<PhotoGrpcMessage> photos = photoRepository.findAll()
                .stream().filter(photo -> photo.getUsername().equals(username))
                .map(entity -> {
                    CountryGrpcMessage countryGrpcMessage = geoClient.getCountryByCode(entity.getCountry());
                    return PhotoGrpcMessage.fromEntity(entity, countryGrpcMessage);
                })
                .toList();

        AllPhotosResponse response = AllPhotosResponse.newBuilder().addAllPhotos(
                photos.stream().map(PhotoGrpcMessage::toGrpcMessage).toList()
        ).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void addUserPhoto(PhotoRequest request, StreamObserver<PhotoResponse> responseObserver) {
        PhotoGrpcMessage photo = fromGrpcMessage(request.getPhoto());

        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setCountry(photo.getCountry().getCode());
        photoEntity.setPhoto(photo.getPhoto());
        photoEntity.setDescription(photo.getDescription());
        photoEntity.setUsername(photo.getUsername());

        PhotoResponse response =
                PhotoResponse.newBuilder().setPhoto(toGrpcMessage(
                        fromEntity(photoRepository.save(photoEntity), photo.getCountry()))).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void editUserPhoto(PhotoRequest request, StreamObserver<PhotoResponse> responseObserver) {
        super.editUserPhoto(request, responseObserver);
    }

    @Override
    public void getAllFriendsPhotos(UsernameRequest request, StreamObserver<AllPhotosResponse> responseObserver) {
        String username = request.getUsername();
        List<String> friends = usersClient.getFriends(username);

        List<PhotoGrpcMessage> photos = photoRepository.findAll()
                .stream().filter(photo -> friends.contains(photo.getUsername()))
                .map(entity -> {
                    CountryGrpcMessage countryGrpcMessage = geoClient.getCountryByCode(entity.getCountry());
                    return PhotoGrpcMessage.fromEntity(entity, countryGrpcMessage);
                })
                .toList();

        AllPhotosResponse response = AllPhotosResponse.newBuilder().addAllPhotos(
                photos.stream().map(PhotoGrpcMessage::toGrpcMessage).toList()
        ).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserPhoto(PhotoIdRequest request, StreamObserver<Empty> responseObserver) {
        super.deleteUserPhoto(request, responseObserver);
    }

}
