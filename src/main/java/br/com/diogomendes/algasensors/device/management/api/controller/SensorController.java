package br.com.diogomendes.algasensors.device.management.api.controller;

import br.com.diogomendes.algasensors.device.management.api.model.SensorInput;
import br.com.diogomendes.algasensors.device.management.api.model.SensorOutput;
import br.com.diogomendes.algasensors.device.management.domain.model.Sensor;
import br.com.diogomendes.algasensors.device.management.domain.model.SensorId;
import br.com.diogomendes.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static br.com.diogomendes.algasensors.device.management.common.IdGenerator.generateTSID;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;

    @GetMapping
    public Page<SensorOutput> search(@PageableDefault Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findAll(pageable);
        return sensors.map(this::convertToModel);
    }

    @GetMapping("{sensorId}")
    public SensorOutput getOne(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        return convertToModel(sensor);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public SensorOutput create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                .id(new SensorId(generateTSID()))
                .name(input.getName())
                .ip(input.getIp())
                .location(input.getLocation())
                .protocol(input.getProtocol())
                .model(input.getModel())
                .enabled(false)
                .build();

        sensor = sensorRepository.saveAndFlush(sensor);

        return convertToModel(sensor);
    }

    @PutMapping("{sensorId}")
    public SensorOutput update(@PathVariable TSID sensorId,
                               @RequestBody SensorInput input) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));

        sensor.setName(input.getName());
        sensor.setIp(input.getIp());
        sensor.setLocation(input.getLocation());
        sensor.setProtocol(input.getProtocol());
        sensor.setModel(input.getModel());

        sensor = sensorRepository.save(sensor);

        return convertToModel(sensor);
    }

    @DeleteMapping("{sensorId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        sensorRepository.delete(sensor);
    }

    @PutMapping("{sensorId}/enable")
    @ResponseStatus(NO_CONTENT)
    public void enable(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        sensor.setEnabled(true);
        sensorRepository.save(sensor);
    }

    @DeleteMapping("{sensorId}/enable")
    @ResponseStatus(NO_CONTENT)
    public void disable(@PathVariable TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        sensor.setEnabled(false);
        sensorRepository.save(sensor);
    }

    private SensorOutput convertToModel(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .enabled(sensor.getEnabled())
                .build();
    }

}
