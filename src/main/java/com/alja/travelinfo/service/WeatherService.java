package com.alja.travelinfo.service;

import com.alja.travelinfo.exceptionHandlers.CityNotFoundException;
import com.alja.travelinfo.model.CityWeather;
import com.alja.travelinfo.exceptionHandlers.InvalidWeatherCastRangeException;
import com.alja.travelinfo.receivedPOJO.ConsolidatedWeather;
import com.alja.travelinfo.receivedPOJO.MajorCityDetails;
import com.alja.travelinfo.receivedPOJO.MajorCityId;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
@Slf4j
public class WeatherService {

    public String country;

    public WeatherService() {
    }

    // == method that returns city info and weather ==
    public CityWeather cityWeather(String cityName) {
        MajorCityDetails majorCityDetails = getCityDetails(cityName);
        CityWeather cityWeather = new CityWeather();

        return getCityInfoAndWeather(majorCityDetails, cityWeather);
    }

    // == method that returns city info, weather and weather-cast ==
    public CityWeather cityWeather(String cityName, int days) {
        MajorCityDetails majorCityDetails = getCityDetails(cityName);
        CityWeather cityWeather = new CityWeather();

        getCityInfoAndWeather(majorCityDetails, cityWeather);

        if (days < 1 || days > 5) {
            throw new InvalidWeatherCastRangeException("Invalid weather cast range: '" + days
                    + "'. Available days range: from '1' to '5'");
        }
        ArrayList<ConsolidatedWeather> weatherCastInDays = new ArrayList<>();
        for (int i = 1; i <= days; i++) {
            weatherCastInDays.add(majorCityDetails.getConsolidated_weather()[i]);
        }
        cityWeather.setWeatherCast(weatherCastInDays);

        return cityWeather;
    }

    // == method that sets fields of CityWeather class ==
    private CityWeather getCityInfoAndWeather(MajorCityDetails majorCityDetails, CityWeather cityWeather) {

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

    // == method that counts day length based on raw Strings of Sun hours
    //    and return clean String with day length info ==
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

    // == method that uses parameter to get woeid number and pass it to another request to
    //    return full data about requested city and its weather ==
    private MajorCityDetails getCityDetails(String cityName) {
        int woeid = getWoeidRequest(cityName);
        MajorCityDetails majorCityDetails = getMajorCityDetailsRequest(woeid, cityName);
        this.country = majorCityDetails.getParent().getTitle();
        return majorCityDetails;
    }

    // == method that builds HTTP request to 'metaweather.com' API
    //    and gets woeid as int response which is needed for further requests ==
    private int getWoeidRequest(String cityName) {

        String apiAddress = "https://www.metaweather.com/api/location/search/?query=";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + cityName).replace(" ", "%20"))).build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        response = response.substring(0, response.length() - 1).substring(1);

        log.info("WeatherService => cityName request: " + request);
        log.info("WeatherService <<== cityName response: " + response);

        MajorCityId majorCityId;
        try {
            ObjectMapper mapper = new ObjectMapper();
            majorCityId = mapper.readValue(response, MajorCityId.class);
        } catch (Exception e) {
            throw new CityNotFoundException("City: '" + cityName + "' was not found");
        }
        return majorCityId.getWoeid();
    }

    // == method that builds HTTP request to 'metaweather.com' API
    //    and maps response to MajorCityDetails POJO ==
    private MajorCityDetails getMajorCityDetailsRequest(int woeid, String cityName) {
        String apiAddress = "https://www.metaweather.com/api/location/";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + woeid + "/"))).build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        log.info("WeatherService => woeid request: " + request);
        log.info("WeatherService <<== woeid response: " + response);

        MajorCityDetails majorCityDetails;
        try {
            ObjectMapper mapper = new ObjectMapper();
            majorCityDetails = mapper.readValue(response, MajorCityDetails.class);
        } catch (Exception e) {
            throw new CityNotFoundException("City: '" + cityName + "' was not found");
        }
        return majorCityDetails;
    }
}