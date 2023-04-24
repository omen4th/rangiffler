package org.rangiffler.model;

import lombok.*;
import org.grpc.rangiffler.grpc.photo.Photo;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PhotoGrpc {
    private UUID id;
    private CountryGrpc country;
    private byte[] photo;
    private String description;
    private String username;

    private String photoPath;

    public static Photo toGrpcMessage(PhotoGrpc photoGrpcMessage) {
        Photo.Builder photoBuilder = Photo.newBuilder()
                .setCountry(CountryGrpc.toGrpcMessage(photoGrpcMessage.getCountry()))
                .setPhoto(new String(photoGrpcMessage.getPhoto(), StandardCharsets.UTF_8))
                .setUsername(photoGrpcMessage.getUsername());

        if (photoGrpcMessage.getId() != null) {
            photoBuilder.setId(photoGrpcMessage.getId().toString());
        }

        if (photoGrpcMessage.getDescription() != null) {
            photoBuilder.setDescription(photoGrpcMessage.getDescription());
        }

        return photoBuilder.build();
    }

    public static PhotoGrpc fromGrpcMessage(Photo photoGrpc) {
        PhotoGrpc.PhotoGrpcBuilder builder = PhotoGrpc.builder()
                .country(CountryGrpc.fromGrpcMessage(photoGrpc.getCountry()))
                .photo(photoGrpc.getPhoto().getBytes())
                .description(photoGrpc.getDescription())
                .username(photoGrpc.getUsername());

        if (!photoGrpc.getId().isEmpty()) {
            builder.id(UUID.fromString(photoGrpc.getId()));
        }

        return builder.build();
    }
}
