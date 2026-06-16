package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class StatusInvalidoParaPagamentoException extends RuntimeException {
    public StatusInvalidoParaPagamentoException(Pedido.Status status) {
        super("Pedido com status " + status + " não pode ser pago. Apenas pedidos APROVADOS podem ser pagos.");
    }
}
