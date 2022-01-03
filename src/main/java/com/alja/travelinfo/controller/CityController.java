package com.alja.travelinfo.controller;

import com.alja.travelinfo.exceptionHandlers.CityErrorResponse;
import com.alja.travelinfo.exceptionHandlers.CityNotFoundException;
import com.alja.travelinfo.exceptionHandlers.SameCityException;
import com.alja.travelinfo.model.CityInformation;
import com.alja.travelinfo.model.CityWeather;
import com.alja.travelinfo.exceptionHandlers.InvalidWeatherCastRangeException;
import com.alja.travelinfo.model.TwoCitiesInfo;
import com.alja.travelinfo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1")
public class CityController {

    private final CityService cityService;
    private final WeatherService weatherService;
    private final CityInformationService cityInformationService;

    @Autowired
    public CityController(CityService cityService, WeatherService weatherService, CityInformationService cityInformationService) {
        this.cityService = cityService;
        this.weatherService = weatherService;
        this.cityInformationService = cityInformationService;
    }

    // == endpoint that returns basic city data as name, coordinates, country, borders and currency
    //    about requested city (works with major world cities) ==
    @GetMapping(path = "/info/{cityName}")
    public CityInformation getCityInformation(@PathVariable("cityName") String cityName) {
        return cityInformationService.getCityInformation(cityName);
    }

    // == endpoint that returns basic geographic info and current weather data
    //    about requested city (works with major world cities) ==
    @GetMapping(path = "/weather/{cityName}")
    public CityWeather getCityWeather(@PathVariable("cityName") String cityName) {
        return weatherService.cityWeather(cityName);
    }

    // == endpoint that returns basic geographic info, current weather data and weather-cast
    //    about requested city (works with major world cities) ==
    @GetMapping(path = "/weather/{cityName}/{days}")
    public CityWeather getCityWeather(@PathVariable("cityName") String cityName,
                                      @PathVariable("days") int days) {
        return weatherService.cityWeather(cityName, days);
    }

    // == endpoint that returns full names and coordinates of requested cities
    //    as well as distance and average flight time between these cities ==
    @GetMapping(path = "/cities/{city1Name}/{city2Name}")
    public TwoCitiesInfo getTwoCitiesInfo(@PathVariable("city1Name") String city1Name,
                                          @PathVariable("city2Name") String city2Name) {
        return cityService.getTravelInfo(city1Name, city2Name);
    }

    // == throws exception when requested weather cast is out of range ==
    @ExceptionHandler
    public ResponseEntity<CityErrorResponse> handleException(InvalidWeatherCastRangeException e) {
        CityErrorResponse error = new CityErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // == throws exception when requested city is not found ==
    @ExceptionHandler
    public ResponseEntity<CityErrorResponse> handleException(CityNotFoundException e) {
        CityErrorResponse error = new CityErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // == throws exception when two requested cities are the same ==
    @ExceptionHandler
    public ResponseEntity<CityErrorResponse> handleException(SameCityException e) {
        CityErrorResponse error = new CityErrorResponse();
        error.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }
}