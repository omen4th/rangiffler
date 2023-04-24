package org.rangiffler.test.grpc;

import io.qameta.allure.AllureId;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.rangiffler.api.RangifflerGeoGrpcClient;
import org.rangiffler.model.Country;
import org.rangiffler.model.CountryGrpc;

import java.util.List;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("[gRPC][rangiffler-geo]: Countries")
@DisplayName("[gRPC][rangiffler-geo]: Countries")
public class RangifflerGeoTest extends BaseGRPCTest {

    static final RangifflerGeoGrpcClient geoClient = new RangifflerGeoGrpcClient();

    @Test
    @AllureId("1101")
    @DisplayName("gRPC: Service rangiffler-geo should return 175 countries")
    @Tag("gRPC")
    void getAllCountriesTest() {
        List<CountryGrpc> allCountries = geoClient.getAllCountries();

        step("Check that response contains 175 countries", () ->
                assertEquals(175, allCountries.size()));
    }

    static Stream<Arguments> getCountryByCodeTest() {
        return Stream.of(
                Arguments.of(Country.UNITED_STATES),
                Arguments.of(Country.NETHERLANDS),
                Arguments.of(Country.BOSNIA_AND_HERZEGOVINA)
        );
    }

    @ParameterizedTest
    @MethodSource
    @AllureId("1102")
    @DisplayName("gRPC: Service rangiffler-geo should return the expected country {0} by provided code")
    @Tag("gRPC")
    void getCountryByCodeTest(Country country) {
        CountryGrpc actualCountry = geoClient.getCountryByCode(country.getCode());

        step("Check received countryName and countryCode", () -> {
            assertEquals(country.getCode(), actualCountry.getCode());
            assertEquals(country.toString(), actualCountry.getName());
        });
    }
}
