package com.alja.travelinfo.receivedPOJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class City {

    private String lat;
    private String lon;
    private String display_name;

    public City() {
    }

    @Override
    public boolean equals(Object o) {
        return display_name.equals(((City) o).getDisplay_name());
    }
}
