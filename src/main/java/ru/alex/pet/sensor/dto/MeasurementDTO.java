package ru.alex.pet.sensor.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public class MeasurementDTO {
    @DecimalMin(value = "-100.0", message = "Значение должно быть не меньше -100")
    @DecimalMax(value = "100.0", message = "Значение должно быть не больше 100")
    @NotNull
    private Double value;

    @NotNull(message = "Не должно быть пустым")
    private Boolean raining;

    @Valid
    @NotNull(message = "Сенсор Не должно быть Null")
    private SensorDTO sensor;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
