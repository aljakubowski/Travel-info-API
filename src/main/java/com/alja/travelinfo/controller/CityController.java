package com.alja.travelinfo.controller;

import com.alja.travelinfo.exceptionHandlers.CityErrorResponse;
import com.alja.travelinfo.exceptionHandlers.CityNotFoundException;
import com.alja.travelinfo.exceptionHandlers.SameCityException;
import com.alja.travelinfo.model.CityWeather;
import com.alja.travelinfo.exceptionHandlers.InvalidWeatherCastRangeException;
import com.alja.travelinfo.receivedPOJO.MajorCityDetails;
import com.alja.travelinfo.model.TwoCitiesInfo;
import com.alja.travelinfo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "api")
public class CityController {

    private final CityService cityService;
    private final WeatherService weatherService;

    @Autowired
    public CityController(CityService cityService, WeatherService weatherService) {
        this.cityService = cityService;
        this.weatherService = weatherService;
    }

    @GetMapping(path = "/v1/cities/{cityName}")
    public String getCityDetails(@PathVariable("cityName") String cityName) {
        return cityService.getCityDetails(cityName);
    }

    @GetMapping(path = "/v1/cities/{city1Name}/{city2Name}")
    public String getAnyCitiesConnection(@PathVariable("city1Name") String city1Name,
                                         @PathVariable("city2Name") String city2Name) {
        return cityService.getTwoCitiesDetails(city1Name, city2Name);
    }


    @GetMapping(path = "/v2/cities/{city1Name}/{city2Name}")
    public TwoCitiesInfo getTwoCitiesInfo(@PathVariable("city1Name") String city1Name,
                                          @PathVariable("city2Name") String city2Name) {
        return cityService.getTravelInfo(city1Name, city2Name);
    }

    @ExceptionHandler
    public ResponseEntity<CityErrorResponse> handleException(CityNotFoundException e) {
        CityErrorResponse error = new CityErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CityErrorResponse> handleException(SameCityException e) {
        CityErrorResponse error = new CityErrorResponse();
        error.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @GetMapping(path = "/v3/weather/{cityName}")
    public MajorCityDetails getMajorCityDetails(@PathVariable("cityName") String cityName) {
        return weatherService.getCityDetails(cityName);
    }


    @GetMapping(path = "/v4/weather/{cityName}")
    public CityWeather getCityWeather(@PathVariable("cityName") String cityName) {
        return weatherService.cityWeather(cityName);
    }

    @GetMapping(path = "/v4/weather/{cityName}/{days}")
    public CityWeather getCityWeatherCast(@PathVariable("cityName") String cityName,
                                          @PathVariable("days") int days) {
        return weatherService.cityWeatherCast(cityName, days);
    }

    @ExceptionHandler
    public ResponseEntity<CityErrorResponse> handleException(InvalidWeatherCastRangeException e) {
        CityErrorResponse error = new CityErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
