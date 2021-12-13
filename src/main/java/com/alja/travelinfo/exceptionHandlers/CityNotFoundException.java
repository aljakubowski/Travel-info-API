package com.alja.travelinfo.exceptionHandlers;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String message) {
        super(message);
    }
}
