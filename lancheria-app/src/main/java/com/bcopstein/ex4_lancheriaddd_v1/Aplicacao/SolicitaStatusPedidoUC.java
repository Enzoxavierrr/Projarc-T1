package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SolicitaStatusPedidoUC {
    private PedidoService pedidoService;

    public SolicitaStatusPedidoUC(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public StatusPedidoResponse run(long id, String cpf) {
        Pedido.Status status = pedidoService.buscaStatusPorIdDoCliente(id, cpf);
        return new StatusPedidoResponse(id, status);
    }
}
