package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.util.List;

public class SubmeterPedidoRequest {
    private String clienteCpf;
    private List<ItemPedidoRequest> itens;
    private String enderecoEntrega;

    public SubmeterPedidoRequest(String clienteCpf, List<ItemPedidoRequest> itens, String enderecoEntrega) {
        this.clienteCpf = clienteCpf;
        this.itens = itens;
        this.enderecoEntrega = enderecoEntrega;
    }

    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }
    public List<ItemPedidoRequest> getItens() { return itens; }
    public String getEnderecoEntrega() { return enderecoEntrega; }

    public static class ItemPedidoRequest {
        private long produtoId;
        private int quantidade;

        public ItemPedidoRequest(long produtoId, int quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
        }

        public long getProdutoId() { return produtoId; }
        public int getQuantidade() { return quantidade; }
    }
}