package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DTOs;

import java.time.LocalDateTime;

public record EntregaStatusDTO(long pedidoId, String status, LocalDateTime dataHora) {
}
