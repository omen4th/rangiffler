package org.rangiffler.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.grpc.rangiffler.grpc.country.Country;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode
public class CountryGrpc {

    private UUID id;
    private String code;
    private String name;

    public static CountryGrpc fromGrpcMessage(Country countryMessage) {
        CountryGrpc.CountryGrpcBuilder builder = CountryGrpc.builder()
                .code(countryMessage.getCode());

        if (countryMessage.getId() != null) {
            builder.id(UUID.fromString((countryMessage.getId())));
        }

        if (countryMessage.getName() != null) {
            builder.name(countryMessage.getName());
        }

        return builder.build();
    }

    public static Country toGrpcMessage(CountryGrpc countryGrpc) {
        Country.Builder countryBuilder = Country.newBuilder()
                .setCode(countryGrpc.getCode());

        if (countryGrpc.getId() != null) {
            countryBuilder.setId(countryGrpc.getId().toString());
        }

        if (countryGrpc.getName() != null) {
            countryBuilder.setName(countryGrpc.getName());
        }

        return countryBuilder.build();
    }
}
