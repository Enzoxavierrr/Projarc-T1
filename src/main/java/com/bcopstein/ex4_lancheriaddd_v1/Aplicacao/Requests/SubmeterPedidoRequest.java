package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

import java.util.List;

public class SubmeterPedidoRequest {
    private String clienteCpf;
    private List<ItemPedidoRequest> itens;

    public SubmeterPedidoRequest(String clienteCpf, List<ItemPedidoRequest> itens) {
        this.clienteCpf = clienteCpf;
        this.itens = itens;
    }

    public String getClienteCpf() { return clienteCpf; }
    public List<ItemPedidoRequest> getItens() { return itens; }

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