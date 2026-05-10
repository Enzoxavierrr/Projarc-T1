package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class PedidoResponse {
    private long id;
    private String status;
    private double valor;
    private double desconto;
    private double impostos;
    private double valorCobrado;
    private List<ItemPedido> itensIndisponiveis;

    public PedidoResponse(Pedido pedido, List<ItemPedido> itensIndisponiveis) {
        this.id = pedido.getId();
        this.status = pedido.getStatus().name();
        this.valor = pedido.getValor();
        this.desconto = pedido.getDesconto();
        this.impostos = pedido.getImpostos();
        this.valorCobrado = pedido.getValorCobrado();
        this.itensIndisponiveis = itensIndisponiveis;
    }

    public long getId() { return id; }
    public String getStatus() { return status; }
    public double getValor() { return valor; }
    public double getDesconto() { return desconto; }
    public double getImpostos() { return impostos; }
    public double getValorCobrado() { return valorCobrado; }
    public List<ItemPedido> getItensIndisponiveis() { return itensIndisponiveis; }
}