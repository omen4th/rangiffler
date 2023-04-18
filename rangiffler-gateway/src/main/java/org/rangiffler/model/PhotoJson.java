package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import org.grpc.rangiffler.grpc.photo.Photo;

@Data
@Builder
public class PhotoJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("country")
    private CountryJson countryJson;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

    public static PhotoJson fromGrpcMessage(Photo photoMessage) {
        return PhotoJson.builder()
                .id(UUID.fromString((photoMessage.getId())))
                .countryJson(CountryJson.fromGrpcMessage(photoMessage.getCountry()))
                .photo(photoMessage.getPhoto())
                .description(photoMessage.getDescription())
                .username(photoMessage.getUsername())
                .build();
    }

    public static Photo toGrpcMessage(PhotoJson photoJson) {
        return Photo.newBuilder()
                .setCountry(CountryJson.toGrpcMessage(photoJson.getCountryJson()))
                .setPhoto(photoJson.getPhoto())
                .setDescription(photoJson.getDescription())
                .setUsername(photoJson.getUsername())
                .build();
    }
}
