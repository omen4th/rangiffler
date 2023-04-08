package org.rangiffler.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.grpc.rangiffler.grpc.geo.*;
import org.rangiffler.data.CountryEntity;
import org.rangiffler.data.repository.CountryRepository;

import java.util.List;

import static io.grpc.Status.NOT_FOUND;

@GrpcService
public class GrpcGeoService extends RangifflerGeoServiceGrpc.RangifflerGeoServiceImplBase {

    private final CountryRepository countryRepository;

    public GrpcGeoService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void getAllCountries(Empty request, StreamObserver<AllCountriesResponse> responseObserver) {
        List<CountryEntity> all = countryRepository.findAll();

        AllCountriesResponse response = AllCountriesResponse.newBuilder()
                .addAllAllCountries(all.stream().map(e -> Country.newBuilder()
                                .setId(String.valueOf(e.getId()))
                                .setCode(e.getCode())
                                .setName(e.getName())
                                .build())
                        .toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCountry(CountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        String countryCode = request.getCode();
        CountryEntity countryEntity = countryRepository.findByCode(countryCode);

        if (countryEntity == null) {
            responseObserver.onError(NOT_FOUND.withDescription("Country with code " + countryCode + " not found.").asRuntimeException());
        } else {
            CountryResponse response = CountryResponse.newBuilder()
                    .setCountry(Country.newBuilder()
                            .setId(String.valueOf(countryEntity.getId()))
                            .setCode(countryEntity.getCode())
                            .setName(countryEntity.getName())
                            .build())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

}
