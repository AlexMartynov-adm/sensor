package ru.alex.pet.sensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alex.pet.sensor.dto.MeasurementDTO;
import ru.alex.pet.sensor.dto.SensorDTO;
import ru.alex.pet.sensor.models.Measurement;
import ru.alex.pet.sensor.models.Sensor;
import ru.alex.pet.sensor.services.MeasurementService;
import ru.alex.pet.sensor.services.SensorService;
import ru.alex.pet.sensor.util.Exceptions.MeasurementError;
import ru.alex.pet.sensor.util.Exceptions.MeasurementNotCreatedException;
import ru.alex.pet.sensor.util.Exceptions.SensorError;
import ru.alex.pet.sensor.util.Exceptions.SensorNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;
    private final SensorService sensorService;

@Autowired
    public MeasurementController(MeasurementService measurementService, ModelMapper modelMapper, SensorService sensorService) {
        this.measurementService = measurementService;
    this.modelMapper = modelMapper;
    this.sensorService = sensorService;

}
    @GetMapping
    public List<MeasurementDTO> getMeasurement(){
    List<MeasurementDTO> measurements = measurementService.findAll().stream().map(m->convertToDTO(m)).collect(Collectors.toList());
    return measurements;
    }
    @GetMapping("/rainyDays")
    public List<MeasurementDTO> getRainyDaysCount(){
        return measurementService.findRainyDays().stream().map(this::convertToDTO).toList();
    }
    @GetMapping("/rainyDays/count")
    public Long getCountRainyDais(){
    return measurementService.countRainyDais();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult){
    if(bindingResult.hasErrors()){
        StringBuilder stringErrors = new StringBuilder();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        errorList.forEach(e->stringErrors.append(e.getField()).append("_").append(e.getDefaultMessage()).append(";"));
        throw new MeasurementNotCreatedException(stringErrors.toString());
    }
        Measurement measurement = convertToMeasurement(measurementDTO);
        String sensorName = measurement.getSensor().getName();
        Sensor sensor = sensorService.findName(sensorName).orElseThrow(()-> new SensorNotFoundException("Нет сенсора с таким названием " + sensorName));
        measurement.setSensor(sensor);
        measurementService.add(measurement);
    return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementError> handleException(MeasurementNotCreatedException e){
        MeasurementError measurementError = new MeasurementError(e.getMessage(),LocalDateTime.now());
    return new ResponseEntity<>(measurementError,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<SensorError> handleExceptionSensor(SensorNotFoundException e){
    SensorError sensorError = new SensorError(e.getMessage(),LocalDateTime.now());
        return new ResponseEntity<>(sensorError,HttpStatus.NOT_FOUND);
    }

    private MeasurementDTO convertToDTO(Measurement measurement){
    MeasurementDTO dto = modelMapper.map(measurement,MeasurementDTO.class);

        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(measurement.getSensor().getName());
        dto.setSensor(sensorDTO);
    return dto;
    }

    private Measurement convertToMeasurement (MeasurementDTO measurementDTO){
    Measurement measurement = modelMapper.map(measurementDTO,Measurement.class);
        Sensor sensor = new Sensor();
        sensor.setName(measurementDTO.getSensor().getName());
        measurement.setSensor(sensor);
        return measurement;
    }
}
