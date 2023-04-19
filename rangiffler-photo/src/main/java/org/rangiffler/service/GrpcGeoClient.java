package org.rangiffler.service;

import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import org.grpc.rangiffler.grpc.geo.CountryRequest;
import org.grpc.rangiffler.grpc.geo.RangifflerGeoServiceGrpc;
import org.rangiffler.model.CountryGrpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GrpcGeoClient {
    private static final Logger LOG = LoggerFactory.getLogger(GrpcGeoClient.class);
    @GrpcClient("grpcGeoClient")
    private RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub rangifflerGeoServiceStub;

    public @Nonnull CountryGrpcMessage getCountryByCode(String countryCode) {
        try {
            return CountryGrpcMessage.fromGrpcMessage(rangifflerGeoServiceStub
                    .getCountry(CountryRequest.newBuilder().setCode(countryCode).build())
                    .getCountry());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
