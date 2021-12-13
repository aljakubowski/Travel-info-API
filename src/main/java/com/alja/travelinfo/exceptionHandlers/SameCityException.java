package com.alja.travelinfo.exceptionHandlers;

public class SameCityException extends RuntimeException{
    public SameCityException(String message) {
        super(message);
    }
}
