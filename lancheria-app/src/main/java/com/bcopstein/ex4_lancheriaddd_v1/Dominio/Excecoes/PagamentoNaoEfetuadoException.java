package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

public class PagamentoNaoEfetuadoException extends RuntimeException {
    public PagamentoNaoEfetuadoException() {
        super("Nao foi possivel efetuar o pagamento do pedido.");
    }
}
