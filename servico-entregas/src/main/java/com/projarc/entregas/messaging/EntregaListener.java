package com.projarc.entregas.messaging;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.projarc.entregas.config.EntregaSimulationProperties;
import com.projarc.entregas.config.RabbitMQConfig;
import com.projarc.entregas.dto.EntregaStatusDTO;
import com.projarc.entregas.dto.PedidoEntregaDTO;

@Component
public class EntregaListener {
    private final RabbitTemplate rabbitTemplate;
    private final TaskScheduler taskScheduler;
    private final EntregaSimulationProperties simulationProperties;

    public EntregaListener(RabbitTemplate rabbitTemplate,
                           TaskScheduler taskScheduler,
                           EntregaSimulationProperties simulationProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.taskScheduler = taskScheduler;
        this.simulationProperties = simulationProperties;
    }

    @RabbitListener(queues = RabbitMQConfig.PEDIDOS_QUEUE)
    public void receber(PedidoEntregaDTO pedido) {
        System.out.println("Pedido recebido para entrega: " + pedido.pedidoId());
        taskScheduler.schedule(
            () -> iniciarTransporte(pedido),
            Instant.now().plus(simulationProperties.inicioTransporteDelay())
        );
    }

    private void iniciarTransporte(PedidoEntregaDTO pedido) {
        publicarStatus(pedido.pedidoId(), "TRANSPORTE");
        taskScheduler.schedule(
            () -> finalizarEntrega(pedido),
            Instant.now().plus(simulationProperties.conclusaoDelay())
        );
    }

    private void finalizarEntrega(PedidoEntregaDTO pedido) {
        publicarStatus(pedido.pedidoId(), "ENTREGUE");
    }

    private void publicarStatus(long pedidoId, String status) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.STATUS_EXCHANGE,
            RabbitMQConfig.STATUS_ROUTING_KEY,
            new EntregaStatusDTO(pedidoId, status, LocalDateTime.now())
        );
        System.out.println("Status publicado para pedido " + pedidoId + ": " + status);
    }
}
