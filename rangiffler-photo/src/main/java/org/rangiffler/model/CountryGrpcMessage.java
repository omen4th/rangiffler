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
        CountryGrpcMessage.CountryGrpcMessageBuilder builder = CountryGrpcMessage.builder()
                .code(countryMessage.getCode());

        if (!countryMessage.getId().isEmpty()) {
            builder.id(UUID.fromString((countryMessage.getId())));
        }

        if (!countryMessage.getName().isEmpty()) {
            builder.name(countryMessage.getName());
        }

        return builder.build();
    }

    public static Country toGrpcMessage(CountryGrpcMessage countryGrpcMessage) {
        return Country.newBuilder()
                .setId(countryGrpcMessage.getId().toString())
                .setCode(countryGrpcMessage.getCode())
                .setName(countryGrpcMessage.getName())
                .build();
    }
}
