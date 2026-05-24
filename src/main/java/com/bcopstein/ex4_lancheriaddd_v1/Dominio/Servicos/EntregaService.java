package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class EntregaService implements IEntregaService {
    private PedidoRepository pedidoRepository;
    private ScheduledExecutorService scheduler;

    public EntregaService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void encaminhar(Pedido pedido) {
        scheduler.schedule(() -> iniciarTransporte(pedido), 5, TimeUnit.SECONDS);
    }

    private void iniciarTransporte(Pedido pedido) {
        pedido.setStatus(Pedido.Status.TRANSPORTE);
        pedidoRepository.atualizarStatus(pedido.getId(), Pedido.Status.TRANSPORTE);
        System.out.println("Pedido em transporte: " + pedido.getId());
        scheduler.schedule(() -> finalizarEntrega(pedido), 5, TimeUnit.SECONDS);
    }

    private void finalizarEntrega(Pedido pedido) {
        pedido.setStatus(Pedido.Status.ENTREGUE);
        pedidoRepository.atualizarStatus(pedido.getId(), Pedido.Status.ENTREGUE);
        System.out.println("Pedido entregue: " + pedido.getId());
    }
}
