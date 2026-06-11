package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public class PagarPedidoResponse {
    private long numeroPedido;

    public PagarPedidoResponse(long numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public long getNumeroPedido() { return numeroPedido; }
}