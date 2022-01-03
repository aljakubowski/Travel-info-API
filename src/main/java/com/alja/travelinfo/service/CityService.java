package com.alja.travelinfo.service;

import com.alja.travelinfo.exceptionHandlers.CityNotFoundException;
import com.alja.travelinfo.exceptionHandlers.SameCityException;
import com.alja.travelinfo.receivedPOJO.City;
import com.alja.travelinfo.model.TwoCitiesInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Service
public class CityService {

    private final TwoCitiesInfo twoCitiesInfo;

    @Autowired
    public CityService(TwoCitiesInfo twoCitiesInfo) {
        this.twoCitiesInfo = twoCitiesInfo;
    }

    public City getCity(String cityName) {
        return createRequest(cityName);
    }

    public String getCityCoords(City city) {
        String[] coords = {city.getLat(), city.getLon()};
        return convertCoordToString(coords);
    }

    public TwoCitiesInfo getTravelInfo(String cityName1, String cityName2) {

        City city1 = createRequest(cityName1);
        City city2 = createRequest(cityName2);

        if (city1.equals(city2)) {
            throw new SameCityException("start city and destination city are the same: " + city1.getDisplay_name());
        }

        String[] coordCity1 = {city1.getLat(), city1.getLon()};
        String[] coordCity2 = {city2.getLat(), city2.getLon()};
        double distance = countDistanceBetween(coordCity1, coordCity2);

        twoCitiesInfo.setStartCity(city1.getDisplay_name());
        twoCitiesInfo.setDestinationCity(city2.getDisplay_name());

        twoCitiesInfo.setStartCityCoord(convertCoordToString(coordCity1));
        twoCitiesInfo.setDestinationCityCoord(convertCoordToString(coordCity2));

        twoCitiesInfo.setDistanceInKm(distance);
        twoCitiesInfo.setAverageFlightTime(averageFlightTime(distance));

        return twoCitiesInfo;
    }

    // == method that returns distance between cities in km,
    //    calculated with formula based on geographic coordinates ==
    private double countDistanceBetween(String[] coord1, String[] coord2) {

        double lat1 = Double.parseDouble(coord1[0]);
        double lon1 = Double.parseDouble(coord1[1]);

        double lat2 = Double.parseDouble(coord2[0]);
        double lon2 = Double.parseDouble(coord2[1]);

        double distance = (Math.sqrt(Math.pow((lat2 - lat1), 2)
                + Math.pow((Math.cos(lat1 * Math.PI / 180) * (lon2 - lon1)), 2)))
                * (40075.704 / 360);
        distance = Math.round(distance * 100.0) / 100.0;
        return distance;
    }

    // == method that returns average flight time which is calculated
    //    with arbitrarily assumed plane speed and boarding time ==
    private String averageFlightTime(double distance) {

        double averagePlaneSpeed = 900.0d;
        int airportTimeHours = 1;
        double flightTime = distance / averagePlaneSpeed;

        int hours = (int) flightTime;
        int minutes = (int) Math.round(flightTime % 1 * 60);

        return hours + airportTimeHours + "h and " + minutes + "min";
    }

    // == method that converts a raw array of coordinates to
    //    String of clean coordinates ==
    private String convertCoordToString(String[] coords) {

        double lat = Double.parseDouble(coords[0]);
        double lon = Double.parseDouble(coords[1]);

        lat = Math.round(lat * 100.0) / 100.0;
        lon = Math.round(lon * 100.0) / 100.0;

        return "lat: " + lat + ", lon: " + lon;
    }

    // == method that builds HTTP request to 'nominatim.openstreetmap.org' API
    //    and maps response to City POJO ==
    private City createRequest(String cityName) {

        String apiAddress = "https://nominatim.openstreetmap.org/?city=";
        String format = "&format=json&limit=1";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + cityName + format)
                .replace(" ", "%20"))).build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        response = response.substring(0, response.length() - 1).substring(1);

        log.info("CityService => request: " + request);
        log.info("CityService <<== response: " + response);

        City city;
        try {
            ObjectMapper mapper = new ObjectMapper();
            city = mapper.readValue(response, City.class);
        } catch (Exception e) {
            throw new CityNotFoundException("City: '" + cityName + "' was not found");
        }
        return city;
    }
}