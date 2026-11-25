package ru.alex.pet.sensor.util.Exceptions;

public class SensorNotFoundException extends RuntimeException{
    public SensorNotFoundException() {
    }

    public SensorNotFoundException(String message) {
        super(message);
    }
}
