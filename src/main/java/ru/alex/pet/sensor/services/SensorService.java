package ru.alex.pet.sensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.pet.sensor.models.Sensor;
import ru.alex.pet.sensor.repositories.SensorRepository;
import ru.alex.pet.sensor.util.Exceptions.SensorNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)// Оптимизация транзакций по умолчанию для операций чтения
public class SensorService {
    private final SensorRepository sensorRepository;
@Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }
    public Sensor findOne(int id){
    return  sensorRepository.findById(id).orElseThrow(SensorNotFoundException::new);
    }
    public Optional<Sensor> findName(String name){
    return sensorRepository.findByName(name);
    }
    public List<Sensor> findAll(){
    return sensorRepository.findAll();
    }

    @Transactional
    public Sensor save(Sensor sensor){
    return sensorRepository.save(sensor);
    }
    @Transactional
    public Sensor update(int id,Sensor updatedSensor){
    // Сначала убеждаемся, что обновляемая запись существует (иначе выбросится 404)
        Sensor existingSensor = findOne(id);
        existingSensor.setName(updatedSensor.getName());
        return sensorRepository.save(existingSensor);
    }

    @Transactional
    public void delete(int id){
        if (!sensorRepository.existsById(id)) {
            throw new SensorNotFoundException("Сенсор с id " + id + " не найден");
        }
    sensorRepository.deleteById(id);
    }

}
