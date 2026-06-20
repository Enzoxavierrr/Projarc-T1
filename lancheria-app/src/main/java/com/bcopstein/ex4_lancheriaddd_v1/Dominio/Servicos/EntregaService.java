package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Mensageria.RabbitMQConfig;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DTOs.PedidoEntregaDTO;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class EntregaService implements IEntregaService {
    private final RabbitTemplate rabbitTemplate;

    public EntregaService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void encaminhar(Pedido pedido) {
        PedidoEntregaDTO mensagem = new PedidoEntregaDTO(
            pedido.getId(),
            pedido.getCliente().getCpf(),
            pedido.getEnderecoEntrega()
        );
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.PEDIDOS_EXCHANGE,
            RabbitMQConfig.PEDIDOS_ROUTING_KEY,
            mensagem
        );
        System.out.println("Pedido encaminhado para entrega: " + pedido.getId());
    }
}
