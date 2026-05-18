package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Exceptions.PedidoNaoEncontradoException;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Exceptions.PedidoNaoPertenceAoClienteException;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Exceptions.StatusInvalidoParaCancelamentoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.IPedidoService;

@Component
public class CancelarPedidoUC {
    private IPedidoService pedidoService;

    @Autowired
    public CancelarPedidoUC(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public void executar(long id, String cpf) {
        Pedido pedido = pedidoService.buscarPorId(id)
            .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        if (!pedido.getCliente().getCpf().equals(cpf)) {
            throw new PedidoNaoPertenceAoClienteException();
        }

        Pedido.Status status = pedido.getStatus();
        if (status != Pedido.Status.APROVADO) {
            throw new StatusInvalidoParaCancelamentoException("Status atual: " + status);
        }

        pedidoService.cancelar(id);
    }
}
