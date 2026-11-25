package ru.alex.pet.sensor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SensorDTO {
    @NotEmpty(message = "Поле имя должно быть заполнено")
    @Size(min = 3,max = 30,message = " название сенсора должно содержать от 3 до 30 символов")
    @Pattern(regexp = "[A-Z][A-Za-z]+",message = "Название сенсора с заглавной буквы")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
