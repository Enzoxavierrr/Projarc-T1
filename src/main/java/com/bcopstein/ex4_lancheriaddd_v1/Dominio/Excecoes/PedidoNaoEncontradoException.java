package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

public class PedidoNaoEncontradoException extends RuntimeException {
    public PedidoNaoEncontradoException(long id) {
        super("Pedido não encontrado: " + id);
    }
}
