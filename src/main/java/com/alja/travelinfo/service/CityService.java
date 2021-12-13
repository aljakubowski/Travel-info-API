package com.alja.travelinfo.service;

import com.alja.travelinfo.exceptionHandlers.CityNotFoundException;
import com.alja.travelinfo.exceptionHandlers.SameCityException;
import com.alja.travelinfo.receivedPOJO.City;
import com.alja.travelinfo.model.TwoCitiesInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// class that handles nominatim.openstreetmap Service
@Service
public class CityService {

    private final TwoCitiesInfo twoCitiesInfo;

    @Autowired
    public CityService(TwoCitiesInfo twoCitiesInfo) {
        this.twoCitiesInfo = twoCitiesInfo;
    }

    public String getCityDetails(String cityName) {
        String outputResult;
        City city = createRequest(cityName);

        outputResult = city.getDisplay_name() + "\nlatitude: " + city.getLat() + "\nlongitude: " + city.getLon();
        return outputResult;
    }

    public String getCityFullName(String cityName) {
        City city = createRequest(cityName);
        return city.getDisplay_name();
    }

    public String getCityCoords(String cityName) {
        City city = createRequest(cityName);
        return "\nlatitude: " + city.getLat() + "\nlongitude: " + city.getLon();
    }


    public TwoCitiesInfo getTravelInfo(String cityName1, String cityName2) {

        City city1 = createRequest(cityName1);
        City city2 = createRequest(cityName2);

        if (city1.equals(city2)) {
            throw new SameCityException("start city and destination city are the same: " + city1.getDisplay_name());
        }

        String[] coordCity1 = {city1.getLat(), city1.getLon()};
        String[] coordCity2 = {city2.getLat(), city2.getLon()};
        double distance = distanceBetween(coordCity1, coordCity2);

        twoCitiesInfo.setStartCity(city1.getDisplay_name());
        twoCitiesInfo.setDestinationCity(city2.getDisplay_name());

        twoCitiesInfo.setStartCityCoord(convertCoordToString(coordCity1));
        twoCitiesInfo.setDestinationCityCoord(convertCoordToString(coordCity2));

        twoCitiesInfo.setDistanceInKm(distance);
        twoCitiesInfo.setAverageFlightTime(averageFlightTime(distance));

        return twoCitiesInfo;
    }

    private double distanceBetween(String[] coord1, String[] coord2) {

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

    private String averageFlightTime(double distance) {

        double averagePlaneSpeed = 900.0d;
        int starAndLandTime = 1;
        double time = distance / averagePlaneSpeed;

        int hours = (int) time;
        int minutes = (int) Math.round(time % 1 * 60);

        return hours + starAndLandTime + "h and " + minutes + "min";
    }

    private String convertCoordToString(String[] coords) {

        double lat = Double.parseDouble(coords[0]);
        double lon = Double.parseDouble(coords[1]);

        lat = Math.round(lat * 100.0) / 100.0;
        lon = Math.round(lon * 100.0) / 100.0;

        return "lat: " + lat + ", lon: " + lon;
    }

    public String getTwoCitiesDetails(String cityName1, String cityName2) {

        String outputResult = "";
        City city1 = createRequest(cityName1);
        City city2 = createRequest(cityName2);

        String[] coordCity1 = {city1.getLat(), city1.getLon()};
        String[] coordCity2 = {city2.getLat(), city2.getLon()};
        double distance = distanceBetween(coordCity1, coordCity2);

        return outputResult + "Distance between:\n" + city1.getDisplay_name() + "\nand\n" + city2.getDisplay_name() +
                "\nis: " + distance + " km";
    }

    private City createRequest(String cityName) {

        String apiAddress = "https://nominatim.openstreetmap.org/?city=";
        String format = "&format=json&limit=1";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create((apiAddress + cityName + format).replace(" ", "%20"))).build();

        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        response = response.substring(0, response.length() - 1).substring(1);

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
