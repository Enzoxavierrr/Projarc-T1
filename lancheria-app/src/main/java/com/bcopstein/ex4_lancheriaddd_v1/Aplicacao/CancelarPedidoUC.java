package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPedidoService;

@Component
public class CancelarPedidoUC {
    private IPedidoService pedidoService;

    public CancelarPedidoUC(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public void executar(long id, String cpf) {
        pedidoService.cancelar(id, cpf);
    }
}
