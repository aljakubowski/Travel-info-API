package com.alja.travelinfo.service;

import com.alja.travelinfo.model.CityInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// class that forms Detailed Information
@Service
@Slf4j
public class CityInformationService {

    private CityInformation cityInformation;
    private WeatherService weatherService;
    private CityService cityService;


    @Autowired
    public CityInformationService(CityInformation cityInformation, WeatherService weatherService, CityService cityService) {
        this.cityInformation = cityInformation;
        this.weatherService = weatherService;
        this.cityService = cityService;
    }

    public CityInformation getCityInformation(String cityName){

        CityInformation cityInformation = new CityInformation();

        cityInformation.setCityName(weatherService.cityWeather(cityName).getCityName());
        cityInformation.setCityFullName(cityService.getCityFullName(cityName));
        cityInformation.setCoordinates(cityService.getCityCoords(cityName));

        cityInformation.setCountry(weatherService.getCityDetails(cityName).getParent().getTitle());

        return null;
    }
}
