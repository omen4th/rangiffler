package org.rangiffler.model;

import lombok.*;
import org.grpc.rangiffler.grpc.photo.Photo;
import org.rangiffler.data.PhotoEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PhotoGrpcMessage {
    private UUID id;
    private CountryGrpcMessage country;
    private byte[] photo;
    private String description;
    private String username;

    public static Photo toGrpcMessage(PhotoGrpcMessage photoGrpcMessage) {
        Photo.Builder photoBuilder = Photo.newBuilder()
                .setId(String.valueOf(photoGrpcMessage.getId()))
                .setCountry(CountryGrpcMessage.toGrpcMessage(photoGrpcMessage.getCountry()))
                .setPhoto(new String(photoGrpcMessage.getPhoto(), StandardCharsets.UTF_8))
                .setUsername(photoGrpcMessage.getUsername());

        if (photoGrpcMessage.getDescription() != null) {
            photoBuilder.setDescription(photoGrpcMessage.getDescription());
        }

        return photoBuilder.build();
    }

    public static PhotoGrpcMessage fromGrpcMessage(Photo photoMessage) {
        return PhotoGrpcMessage.builder()
                .country(CountryGrpcMessage.fromGrpcMessage(photoMessage.getCountry()))
                .photo(photoMessage.getPhoto().getBytes())
                .description(photoMessage.getDescription())
                .username(photoMessage.getUsername())
                .build();
    }

    public static PhotoGrpcMessage fromEntity(PhotoEntity entity, CountryGrpcMessage countryGrpcMessage) {
        return PhotoGrpcMessage.builder()
                .id(entity.getId())
                .country(countryGrpcMessage)
                .photo(entity.getPhoto())
                .description(entity.getDescription())
                .username(entity.getUsername())
                .build();
    }
}
