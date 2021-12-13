package com.alja.travelinfo.model;

import com.alja.travelinfo.receivedPOJO.ConsolidatedWeather;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Data
@Service
public class CityWeather {

    private String cityName;
    private String date;
    private String time;
    private String sunRise;
    private String sunSet;
    private String dayLength;
    private ConsolidatedWeather weather;
    private ArrayList<ConsolidatedWeather> weatherCast;
}
