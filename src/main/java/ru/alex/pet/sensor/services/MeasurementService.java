package ru.alex.pet.sensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.pet.sensor.models.Measurement;
import ru.alex.pet.sensor.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
@Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }
    public List<Measurement> findAll(){
    return measurementRepository.findAll();
    }
    public List<Measurement> findRainyDays(){
    return measurementRepository.findByRainingTrue();
    }
    public Long countRainyDais(){
    return measurementRepository.countByRainingTrue();
}
    @Transactional
public void add(Measurement measurement){
    measurement.setMeasurementDate(LocalDateTime.now());
    measurementRepository.save(measurement);
}



}
