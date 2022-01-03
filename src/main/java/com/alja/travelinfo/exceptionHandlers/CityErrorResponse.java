package com.alja.travelinfo.exceptionHandlers;

import lombok.Data;

@Data
public class CityErrorResponse {

    private int status;
    private String message;
    private long timeStamp;

    public CityErrorResponse() {
    }
}