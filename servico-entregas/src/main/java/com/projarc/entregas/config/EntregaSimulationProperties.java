package com.projarc.entregas.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("entregas.simulacao")
public record EntregaSimulationProperties(Duration inicioTransporteDelay, Duration conclusaoDelay) {
}
