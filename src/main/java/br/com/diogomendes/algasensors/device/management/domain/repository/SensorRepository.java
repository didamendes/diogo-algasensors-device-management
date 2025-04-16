package br.com.diogomendes.algasensors.device.management.domain.repository;

import br.com.diogomendes.algasensors.device.management.domain.model.Sensor;
import br.com.diogomendes.algasensors.device.management.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
}
