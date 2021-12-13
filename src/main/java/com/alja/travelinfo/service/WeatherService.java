package com.alja.travelinfo.service;

import com.alja.travelinfo.exceptionHandlers.CityNotFoundException;
import com.alja.travelinfo.model.CityWeather;
import com.alja.travelinfo.exceptionHandlers.InvalidWeatherCastRangeException;
import com.alja.travelinfo.receivedPOJO.ConsolidatedWeather;
import com.alja.travelinfo.receivedPOJO.MajorCityDetails;
import com.alja.travelinfo.receivedPOJO.MajorCityId;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

// class that handles metaweather.com Service
@Service
@Slf4j
public class WeatherService {

    private String cityName;
    private CityWeather cityWeather;

    @Autowired
    public WeatherService(CityWeather cityWeather) {
        this.cityWeather = cityWeather;
    }

    public CityWeather cityWeatherCast(String cityName, int days) {

        if (days < 1 || days > 5) {
            throw new InvalidWeatherCastRangeException("Invalid weather cast range: '" + days
                    + "'. Available range: from 1 to 5 days");
        }

        CityWeather cityWeather = cityWeather(cityName);
        MajorCityDetails majorCityDetails = getCityDetails(cityName);

        ArrayList<ConsolidatedWeather> weatherCastInDays = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            weatherCastInDays.add(majorCityDetails.getConsolidated_weather()[i]);
        }
        cityWeather.setWeatherCast(weatherCastInDays);

        return cityWeather;
    }

    public CityWeather cityWeather(String cityName) {
        MajorCityDetails majorCityDetails = getCityDetails(cityName);

        CityWeather cityWeather = new CityWeather();
        cityWeather.setCityName(majorCityDetails.getTitle());
        cityWeather.setDate(majorCityDetails.getTime().substring(0, 10));
        cityWeather.setTime(majorCityDetails.getTime().substring(11, 16));

        String sunRise = majorCityDetails.getSun_rise().substring(11, 16);
        String sunSet = majorCityDetails.getSun_set().substring(11, 16);
        String[] sunHours = {sunRise, sunSet};

        cityWeather.setSunRise(sunRise);
        cityWeather.setSunSet(sunSet);
        cityWeather.setDayLength(countDayLength(sunHours));
        cityWeather.setWeather(majorCityDetails.getConsolidated_weather()[0]);

        return cityWeather;
    }

    private String countDayLength(String[] hours) {

        int hourStart = Integer.parseInt(hours[0].substring(0, 2));
        int minStart = Integer.parseInt(hours[0].substring(3, 5));
        int hourEnd = Integer.parseInt(hours[1].substring(0, 2));
        int minEnd = Integer.parseInt(hours[1].substring(3, 5));

        int minuteLength = Math.abs(hourEnd * 60 + minEnd - hourStart * 60 - minStart);
        int hourDayLength = minuteLength / 60;
        int minDayLength = minuteLength % 60;

        return hourDayLength + " hours, " + minDayLength + " minutes";
    }


    public MajorCityDetails getCityDetails(String cityName) {
        int woeid = getWoeidRequest(cityName);
        MajorCityDetails city = getMajorCityDetailsRequest(woeid);

        return city;
    }

    private MajorCityDetails getMajorCityDetailsRequest(int woeid) {
        String apiAddress = "https://www.metaweather.com/api/location/";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + woeid + "/"))).build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        MajorCityDetails majorCityDetails;
        try {
            ObjectMapper mapper = new ObjectMapper();
            majorCityDetails = mapper.readValue(response, MajorCityDetails.class);
        } catch (Exception e) {
            throw new CityNotFoundException("City: '" + cityName + "' was not found");
        }
        log.info("=== > > > title is:" + majorCityDetails.getTitle());
        return majorCityDetails;
    }

    private int getWoeidRequest(String cityName) {

        String apiAddress = "https://www.metaweather.com/api/location/search/?query=";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + cityName).replace(" ", "%20"))).build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        response = response.substring(0, response.length() - 1).substring(1);

        MajorCityId majorCityId;
        try {
            ObjectMapper mapper = new ObjectMapper();
            majorCityId = mapper.readValue(response, MajorCityId.class);
        } catch (Exception e) {
            throw new CityNotFoundException("City: '" + cityName + "' was not found");
        }
        this.cityName = majorCityId.getTitle();
        return majorCityId.getWoeid();
    }
}
