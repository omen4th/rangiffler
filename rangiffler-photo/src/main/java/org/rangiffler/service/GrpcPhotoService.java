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
import java.util.UUID;

import static org.rangiffler.model.PhotoGrpcMessage.*;

@GrpcService
public class GrpcPhotoService extends RangifflerPhotoServiceGrpc.RangifflerPhotoServiceImplBase {
    private final PhotoRepository photoRepository;
    private final GrpcGeoClient geoClient;
    private final GrpcUsersClient usersClient;

    private static final Empty EMPTY = Empty.getDefaultInstance();

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

        CountryGrpcMessage country = geoClient.getCountryByCode(photo.getCountry().getCode());

        PhotoResponse response =
                PhotoResponse.newBuilder().setPhoto(toGrpcMessage(
                        fromEntity(photoRepository.save(photoEntity), country)
                )).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void editUserPhoto(PhotoRequest request, StreamObserver<PhotoResponse> responseObserver) {
        PhotoGrpcMessage photo = fromGrpcMessage(request.getPhoto());

        PhotoEntity photoEntity = photoRepository.findById(photo.getId()).orElse(null);
        photoEntity.setDescription(request.getPhoto().getDescription());
        photoEntity.setCountry(request.getPhoto().getCountry().getCode());

        CountryGrpcMessage country = geoClient.getCountryByCode(photo.getCountry().getCode());

        PhotoResponse response =
                PhotoResponse.newBuilder().setPhoto(toGrpcMessage(
                        fromEntity(photoRepository.save(photoEntity), country)
                )).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
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
        photoRepository.deleteById(UUID.fromString(request.getId()));
        responseObserver.onNext(EMPTY);
        responseObserver.onCompleted();
    }

}
