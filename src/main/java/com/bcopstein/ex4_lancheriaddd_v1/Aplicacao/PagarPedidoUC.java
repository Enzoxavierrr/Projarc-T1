package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PagarPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ICozinhaService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPagamentoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class PagarPedidoUC {
    private PedidoService pedidoService;
    private IPagamentoService pagamentoService;
    private ICozinhaService cozinhaService;

    @Autowired
    public PagarPedidoUC(PedidoService pedidoService, IPagamentoService pagamentoService, ICozinhaService cozinhaService) {
        this.pedidoService = pedidoService;
        this.pagamentoService = pagamentoService;
        this.cozinhaService = cozinhaService;
    }

    public PagarPedidoResponse executar(long id, PagarPedidoRequest request) {
        Pedido pedido = pedidoService.pagar(id, request.getCpf());
        pagamentoService.pagar(pedido);
        cozinhaService.chegadaDePedido(pedido);
        return new PagarPedidoResponse(id);
    }
}