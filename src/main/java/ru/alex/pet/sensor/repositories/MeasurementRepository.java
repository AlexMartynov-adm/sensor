package ru.alex.pet.sensor.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alex.pet.sensor.models.Measurement;

import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement,Integer> {
    /**
     * Загружает все измерения вместе со связанными сенсорами одним JOIN-запросом,
     * предотвращая проблему N+1 при последующем маппинге в DTO.
     */
    @Override
    @EntityGraph(attributePaths = {"sensor"})
    List<Measurement> findAll();

    /**
     * Возвращает измерения с зафиксированным дождём, сразу подгружая данные сенсора.
     */
    @EntityGraph(attributePaths = {"sensor"})
    List<Measurement> findByRainingTrue();

    Long countByRainingTrue();
}
