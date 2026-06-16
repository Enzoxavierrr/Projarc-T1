package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.List;

public class ResultadoPedido {
    private Pedido pedido;
    private List<ItemPedido> itensIndisponiveis;

    public ResultadoPedido(Pedido pedido, List<ItemPedido> itensIndisponiveis) {
        this.pedido = pedido;
        this.itensIndisponiveis = itensIndisponiveis;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public List<ItemPedido> getItensIndisponiveis() {
        return itensIndisponiveis;
    }
}
