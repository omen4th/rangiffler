package org.rangiffler.controller;

import org.rangiffler.model.CountryJson;
import org.rangiffler.service.GrpcGeoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GeoController {

    private final GrpcGeoClient geoClient;

    @Autowired
    public GeoController(GrpcGeoClient geoClient) {
        this.geoClient = geoClient;
    }

    @GetMapping("/countries")
    public List<CountryJson> getAllCountries() {
        return geoClient.getAllCountries();
    }

}
