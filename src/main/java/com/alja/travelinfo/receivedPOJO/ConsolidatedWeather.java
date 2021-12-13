package com.alja.travelinfo.receivedPOJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ConsolidatedWeather {

    private String applicable_date;
    private String weather_state_name;
    private int the_temp;
    private int wind_speed;
    private int air_pressure;
    private int humidity;

}
