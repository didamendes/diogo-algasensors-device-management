package br.com.diogomendes.algasensors.device.management.api.client;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

public class SensorMonitoringClientBadGatewayException extends RuntimeException {
}
