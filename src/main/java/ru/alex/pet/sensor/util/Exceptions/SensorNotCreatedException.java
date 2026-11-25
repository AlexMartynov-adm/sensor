package ru.alex.pet.sensor.util.Exceptions;

public class SensorNotCreatedException extends RuntimeException {
    public SensorNotCreatedException() {
    }

    public SensorNotCreatedException(String message) {
        super(message);
    }
}
