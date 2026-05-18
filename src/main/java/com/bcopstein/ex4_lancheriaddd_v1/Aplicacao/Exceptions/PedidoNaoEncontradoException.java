package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Exceptions;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(long id) {
        super("Pedido não encontrado: " + id);
    }
}
