package ru.alex.pet.sensor.util;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alex.pet.sensor.models.Sensor;
import ru.alex.pet.sensor.repositories.SensorRepository;
/**
 * Кастомный валидатор для проверки уникальности имени сенсора в базе данных.
 */
@Component
public class SensorValidator implements Validator {
    private final SensorRepository sensorRepository;

    public SensorValidator(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Sensor sensor = (Sensor) o;
        // Проверяем, существует ли в БД сенсор с таким же именем.
        // Сравнение ID позволяет корректно обновлять сенсор (PATCH), не конфликтуя с самим собой.
        sensorRepository.findByName(sensor.getName()).ifPresent(existingSensor -> {
            if (existingSensor.getId() != sensor.getId()) {
                errors.rejectValue("name", "", "Сенсор с таким названием уже существует");
            }
        });

    }



}
