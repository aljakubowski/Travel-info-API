package com.alja.travelinfo.exceptionHandlers;

public class InvalidWeatherCastRangeException extends RuntimeException{
    public InvalidWeatherCastRangeException(String message) {
        super(message);
    }
}
