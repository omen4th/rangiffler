package org.rangiffler.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.grpc.rangiffler.grpc.country.Country;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
public class CountryGrpcMessage {

    private UUID id;
    private String code;
    private String name;

    public static CountryGrpcMessage fromGrpcMessage(Country countryMessage) {
        return CountryGrpcMessage.builder()
                .id(UUID.fromString((countryMessage.getId())))
                .code(countryMessage.getCode())
                .name(countryMessage.getName())
                .build();
    }

    public static Country toGrpcMessage(CountryGrpcMessage countryGrpcMessage) {
        return Country.newBuilder()
                .setId(countryGrpcMessage.getId().toString())
                .setCode(countryGrpcMessage.getCode())
                .setName(countryGrpcMessage.getName())
                .build();
    }
}
