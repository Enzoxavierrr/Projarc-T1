package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class CozinhaService implements ICozinhaService {
    private Queue<Pedido> filaEntrada;
    private Pedido emPreparacao;
    private Queue<Pedido> filaSaida;

    private ScheduledExecutorService scheduler;
    private IEntregaService entregaService;
    private PedidoRepository pedidoRepository;

    public CozinhaService(IEntregaService entregaService, PedidoRepository pedidoRepository) {
        this.entregaService = entregaService;
        this.pedidoRepository = pedidoRepository;
        filaEntrada = new LinkedBlockingQueue<Pedido>();
        emPreparacao = null;
        filaSaida = new LinkedBlockingQueue<Pedido>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    private synchronized void colocaEmPreparacao(Pedido pedido) {
        pedido.setStatus(Pedido.Status.PREPARACAO);
        pedidoRepository.atualizarStatus(pedido.getId(), Pedido.Status.PREPARACAO);
        emPreparacao = pedido;
        System.out.println("Pedido em preparacao: " + pedido.getId());
        scheduler.schedule(() -> pedidoPronto(), 5, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void chegadaDePedido(Pedido p) {
        p.setStatus(Pedido.Status.AGUARDANDO);
        pedidoRepository.atualizarStatus(p.getId(), Pedido.Status.AGUARDANDO);
        filaEntrada.add(p);
        System.out.println("Pedido na fila de entrada: " + p.getId());
        if (emPreparacao == null) {
            colocaEmPreparacao(filaEntrada.poll());
        }
    }

    @Override
    public synchronized void pedidoPronto() {
        emPreparacao.setStatus(Pedido.Status.PRONTO);
        pedidoRepository.atualizarStatus(emPreparacao.getId(), Pedido.Status.PRONTO);
        filaSaida.add(emPreparacao);
        System.out.println("Pedido na fila de saida: " + emPreparacao.getId());
        entregaService.encaminhar(emPreparacao);
        emPreparacao = null;
        if (!filaEntrada.isEmpty()) {
            Pedido prox = filaEntrada.poll();
            scheduler.schedule(() -> colocaEmPreparacao(prox), 1, TimeUnit.SECONDS);
        }
    }
}
