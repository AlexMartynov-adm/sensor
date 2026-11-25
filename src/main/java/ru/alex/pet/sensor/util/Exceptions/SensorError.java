package ru.alex.pet.sensor.util.Exceptions;

import java.time.LocalDateTime;

public class SensorError {
    private String message;
    private LocalDateTime timeError;

    public SensorError(String message, LocalDateTime timeError) {
        this.message = message;
        this.timeError = timeError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeError() {
        return timeError;
    }

    public void setTimeError(LocalDateTime timeError) {
        this.timeError = timeError;
    }
}
