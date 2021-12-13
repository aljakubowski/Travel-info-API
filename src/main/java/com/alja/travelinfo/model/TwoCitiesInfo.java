package com.alja.travelinfo.model;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class TwoCitiesInfo {

    public String startCity;
    private String startCityCoord;
    private String destinationCity;
    private String destinationCityCoord;
    private double distanceInKm;
    private String averageFlightTime;

    public TwoCitiesInfo() {
    }

}
