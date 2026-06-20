package com.projarc.entregas.dto;

import java.time.LocalDateTime;

public record EntregaStatusDTO(long pedidoId, String status, LocalDateTime dataHora) {
}
