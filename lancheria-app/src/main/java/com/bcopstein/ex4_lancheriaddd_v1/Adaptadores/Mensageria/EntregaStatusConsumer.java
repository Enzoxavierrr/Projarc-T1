package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Mensageria;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DTOs.EntregaStatusDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class EntregaStatusConsumer {
    private final PedidoRepository pedidoRepository;

    public EntregaStatusConsumer(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.STATUS_QUEUE)
    public void receber(EntregaStatusDTO evento) {
        Pedido.Status status;
        try {
            status = Pedido.Status.valueOf(evento.status());
        } catch (IllegalArgumentException exception) {
            System.err.println("Status de entrega ignorado: " + evento.status());
            return;
        }

        if (status != Pedido.Status.TRANSPORTE && status != Pedido.Status.ENTREGUE) {
            System.err.println("Status de entrega nao permitido: " + evento.status());
            return;
        }

        pedidoRepository.atualizarStatus(evento.pedidoId(), status);
        System.out.println("Status de entrega atualizado: pedido=" + evento.pedidoId() + ", status=" + status);
    }
}
