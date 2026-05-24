package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoListagemResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class ListarPedidosEntreguesUC {
    private PedidoService pedidoService;

    public ListarPedidosEntreguesUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public List<PedidoListagemResponse> run(LocalDate inicio, LocalDate fim) {
        return pedidoService.listarEntreguesEntreDatas(inicio, fim)
            .stream()
            .map(p -> new PedidoListagemResponse(
                p.getId(),
                p.getCliente().getCpf(),
                p.getCliente().getNome(),
                p.getStatus().name(),
                p.getValorCobrado() / 100.0,
                p.getEnderecoEntrega()
            ))
            .toList();
    }
}
