package org.rangiffler.api;

import com.google.protobuf.Empty;
import io.qameta.allure.Step;
import org.grpc.rangiffler.grpc.geo.CountryRequest;
import org.grpc.rangiffler.grpc.geo.RangifflerGeoServiceGrpc;
import org.rangiffler.model.CountryGrpc;

import java.util.List;

public class RangifflerGeoGrpcClient extends GrpcClient {

    public RangifflerGeoGrpcClient() {
        super(CFG.geoGrpcAddress(), CFG.geoGrpcPort());
    }

    private final RangifflerGeoServiceGrpc.RangifflerGeoServiceBlockingStub rangifflerGeoServiceStub =
            RangifflerGeoServiceGrpc.newBlockingStub(CHANNEL);

    @Step("Request GetAllCountries to rangiffler-geo")
    public List<CountryGrpc> getAllCountries() {
        return rangifflerGeoServiceStub
                .getAllCountries(Empty.getDefaultInstance())
                .getAllCountriesList()
                .stream().map(CountryGrpc::fromGrpcMessage)
                .toList();
    }

    @Step("Request GetCountry to rangiffler-geo")
    public CountryGrpc getCountryByCode(String countryCode) {
        return CountryGrpc.fromGrpcMessage(rangifflerGeoServiceStub
                .getCountry(CountryRequest.newBuilder().setCode(countryCode).build())
                .getCountry());
    }

}
