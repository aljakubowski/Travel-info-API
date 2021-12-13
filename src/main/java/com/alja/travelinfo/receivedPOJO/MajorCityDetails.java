package com.alja.travelinfo.receivedPOJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MajorCityDetails {

    private String title;
    private String time;
    private String sun_rise;
    private String sun_set;
    private Parent parent;
    private ConsolidatedWeather[] consolidated_weather;

}
