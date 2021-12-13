package com.alja.travelinfo.receivedPOJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MajorCityId {

    private String title;
    private int woeid;

    public MajorCityId() {
    }
}
