package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.grpc.rangiffler.grpc.geo.Country;

import java.util.UUID;

@Data
@Builder
public class CountryJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    public static CountryJson fromGrpcMessage(Country countryMessage) {
        return CountryJson.builder()
                .id(UUID.fromString((countryMessage.getId())))
                .code(countryMessage.getCode())
                .name(countryMessage.getName())
                .build();
    }
}
