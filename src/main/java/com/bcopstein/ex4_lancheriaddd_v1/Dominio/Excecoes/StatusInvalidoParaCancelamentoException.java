package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class StatusInvalidoParaCancelamentoException extends RuntimeException {
    public StatusInvalidoParaCancelamentoException(Pedido.Status status) {
        super("Pedido com status " + status + " não pode ser cancelado. Apenas pedidos APROVADOS podem ser cancelados.");
    }
}
