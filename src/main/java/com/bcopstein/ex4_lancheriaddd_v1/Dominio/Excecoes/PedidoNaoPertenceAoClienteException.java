package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

public class PedidoNaoPertenceAoClienteException extends RuntimeException {
    public PedidoNaoPertenceAoClienteException(long pedidoId, String cpf) {
        super("Pedido " + pedidoId + " não pertence ao cliente " + cpf);
    }
}
