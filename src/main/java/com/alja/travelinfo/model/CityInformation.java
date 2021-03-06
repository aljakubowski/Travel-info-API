package com.alja.travelinfo.model;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class CityInformation {

    private String cityName;
    private String cityFullName;
    private String coordinates;

    private String country;
    private String[] countryBorders;
    private String currencySymbol;

}