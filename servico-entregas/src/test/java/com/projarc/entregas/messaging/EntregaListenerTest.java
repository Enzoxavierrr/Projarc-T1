package com.projarc.entregas.messaging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.TaskScheduler;

import com.projarc.entregas.config.EntregaSimulationProperties;
import com.projarc.entregas.config.RabbitMQConfig;
import com.projarc.entregas.dto.EntregaStatusDTO;
import com.projarc.entregas.dto.PedidoEntregaDTO;

class EntregaListenerTest {
    @Test
    void publicaTransporteEDepoisEntrega() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        TaskScheduler taskScheduler = mock(TaskScheduler.class);
        EntregaListener listener = new EntregaListener(
            rabbitTemplate,
            taskScheduler,
            new EntregaSimulationProperties(Duration.ofSeconds(4), Duration.ofSeconds(4))
        );
        PedidoEntregaDTO pedido = new PedidoEntregaDTO(25L, "123", "Rua A");
        ArgumentCaptor<Runnable> inicioTransporte = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Runnable> conclusaoEntrega = ArgumentCaptor.forClass(Runnable.class);

        listener.receber(pedido);
        verify(taskScheduler).schedule(inicioTransporte.capture(), any(java.time.Instant.class));
        inicioTransporte.getValue().run();
        verify(taskScheduler, times(2)).schedule(conclusaoEntrega.capture(), any(java.time.Instant.class));
        conclusaoEntrega.getAllValues().get(1).run();

        ArgumentCaptor<EntregaStatusDTO> status = ArgumentCaptor.forClass(EntregaStatusDTO.class);
        verify(rabbitTemplate, times(2)).convertAndSend(
            eq(RabbitMQConfig.STATUS_EXCHANGE),
            eq(RabbitMQConfig.STATUS_ROUTING_KEY),
            status.capture()
        );
        assertEquals("TRANSPORTE", status.getAllValues().get(0).status());
        assertEquals("ENTREGUE", status.getAllValues().get(1).status());
    }
}
