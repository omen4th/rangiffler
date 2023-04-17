package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.grpc.rangiffler.grpc.country.Country;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
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

    public static Country toGrpcMessage(CountryJson countryJson) {
        return Country.newBuilder()
                .setId(countryJson.getId().toString())
                .setCode(countryJson.getCode())
                .setName(countryJson.getName())
                .build();
    }
}
