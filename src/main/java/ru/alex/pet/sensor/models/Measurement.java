package ru.alex.pet.sensor.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "measurement")
public class Measurement {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    @DecimalMin(value = "-100.0", message = "Значение должно быть не меньше -100")
    @DecimalMax(value = "100.0", message = "Значение должно быть не больше 100")
    @NotNull
    private Double value;

    @Column(name = "raining")
    @NotNull(message = "Не должно быть пустым")
    private Boolean raining;

    @Column(name = "measurement_date")
    private LocalDateTime measurementDate;

    @ManyToOne
    @JoinColumn(name = "sensor_id",referencedColumnName = "id")
    @NotNull(message = "Сенсор Не должно быть Null")
    @Valid
    private Sensor sensor;

    public Measurement() {
    }

    public Measurement(Double value, Boolean raining) {
        this.value = value;
        this.raining = raining;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public LocalDateTime getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDateTime measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
