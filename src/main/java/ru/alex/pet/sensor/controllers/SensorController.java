package ru.alex.pet.sensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alex.pet.sensor.dto.SensorDTO;
import ru.alex.pet.sensor.models.Sensor;
import ru.alex.pet.sensor.services.SensorService;
import ru.alex.pet.sensor.util.Exceptions.SensorError;
import ru.alex.pet.sensor.util.Exceptions.SensorNotCreatedException;
import ru.alex.pet.sensor.util.Exceptions.SensorNotFoundException;
import ru.alex.pet.sensor.util.SensorValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @GetMapping
    public List<SensorDTO> getSensors() {
        return sensorService.findAll().stream().map(s -> convertToSensorDTO(s)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SensorDTO getSensor(@PathVariable int id) {
        return convertToSensorDTO(sensorService.findOne(id));
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {

        sensorValidator.validate(convertTOSensor(sensorDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(e -> errorMessage.append(e.getField()).append("_").append(e.getDefaultMessage()).append(";"));

            throw new SensorNotCreatedException(errorMessage.toString());
        }
        sensorService.save(convertTOSensor(sensorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SensorDTO> update(@PathVariable("id") int id, @RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {
        Sensor sensor = convertTOSensor(sensorDTO);
        sensor.setId(id);
        sensorValidator.validate(sensor, bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(e -> stringBuilder.append(e.getField()).append("_").append(e.getDefaultMessage()).append(";"));
            throw new SensorNotCreatedException(stringBuilder.toString());
        }
        sensorService.update(id, sensor);
        return ResponseEntity.ok(convertToSensorDTO(sensor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        sensorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Этот метод для исключения SensorNotFoundException поиска
    @ExceptionHandler
    private ResponseEntity<SensorError> handleException(SensorNotFoundException e) {

        SensorError sensorResponse = new SensorError("Сенсора нет по такому id", LocalDateTime.now());
        return new ResponseEntity<>(sensorResponse, HttpStatus.NOT_FOUND);

    }

    //Этот метод для исключения SensorNotCreatedException
    @ExceptionHandler
    private ResponseEntity<SensorError> handleException(SensorNotCreatedException e) {
        SensorError sensorResponse = new SensorError(e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(sensorResponse, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertTOSensor(SensorDTO sensorDTO) {

        return modelMapper.map(sensorDTO, Sensor.class);
    }


    private SensorDTO convertToSensorDTO(Sensor sensor) {//принимает модель и переделываем ее в DTO
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
