package ru.alex.pet.sensor.util.Exceptions;

public class MeasurementNotCreatedException extends RuntimeException {
    public MeasurementNotCreatedException() {
    }

    public MeasurementNotCreatedException(String message) {
        super(message);
    }
}
