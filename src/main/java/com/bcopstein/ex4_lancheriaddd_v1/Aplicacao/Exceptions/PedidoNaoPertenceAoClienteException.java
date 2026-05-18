package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Exceptions;

public class PedidoNaoPertenceAoClienteException extends RuntimeException {
    public PedidoNaoPertenceAoClienteException() {
        super("Pedido não pertence ao cliente informado");
    }
}
