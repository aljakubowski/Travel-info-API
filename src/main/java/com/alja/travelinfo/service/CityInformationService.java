package com.alja.travelinfo.service;

import com.alja.travelinfo.exceptionHandlers.CityNotFoundException;
import com.alja.travelinfo.model.CityInformation;
import com.alja.travelinfo.receivedPOJO.City;
import com.alja.travelinfo.receivedPOJO.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class CityInformationService {

    private final CityInformation cityInformation;
    private final WeatherService weatherService;
    private final CityService cityService;

    private Country country;

    @Autowired
    public CityInformationService(CityInformation cityInformation, WeatherService weatherService, CityService cityService) {
        this.cityInformation = cityInformation;
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    // == method that sets CityInformation object ==
    public CityInformation getCityInformation(String cityName) {

        cityInformation.setCityName(weatherService.cityWeather(cityName).getCityName());
        City city = cityService.getCity(cityName);
        cityInformation.setCityFullName(city.getDisplay_name());
        cityInformation.setCoordinates(cityService.getCityCoords(city));

        String countryName = weatherService.country;
        if (!checkIfCountryExists(countryName)) {
            String[] displayName = city.getDisplay_name().split(", ");
            countryName = displayName[displayName.length - 1];
        }

        cityInformation.setCountry(countryName);
        this.country = getCurrencyAndBorders(countryName);

        cityInformation.setCountryBorders(this.country.getBorders());
        cityInformation.setCurrencySymbol(this.country.getCurrencies().toString().substring(1, 4));

        return cityInformation;
    }

    // == method that builds HTTP request to 'restcountries.com' API
    //    and maps response to Country POJO ==
    private Country getCurrencyAndBorders(String countryName) {

        String apiAddress = "https://restcountries.com/v3.1/name/";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + countryName).replace(" ", "%20"))).build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        response = response.substring(0, response.length() - 1).substring(1);

        log.info("CityInformationService => countryName request: " + request);
        log.info("CityInformationService <<== countryName response: " + response);

        try {
            ObjectMapper mapper = new ObjectMapper();
            this.country = mapper.readValue(response, Country.class);
        } catch (Exception e) {
            throw new CityNotFoundException("Country: '" + countryName + "' was not found");
        }
        return this.country;
    }

    // == method made only for US cities which return State as Country
    //    (States are not available in 'restcountries.com' API) ==
    private boolean checkIfCountryExists(String countryName) {

        String apiAddress = "https://restcountries.com/v3.1/name/";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + countryName).replace(" ", "%20"))).build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        response = response.substring(0, response.length() - 1).substring(1);

        log.info("CityInformationService => checkCountry request: " + request);
        log.info("CityInformationService <<== checkCountry response: " + response);

        try {
            ObjectMapper mapper = new ObjectMapper();
            this.country = mapper.readValue(response, Country.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}