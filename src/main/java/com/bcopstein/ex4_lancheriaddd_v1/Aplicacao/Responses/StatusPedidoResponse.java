package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class StatusPedidoResponse {
    private long id;
    private String status;

    public StatusPedidoResponse(Pedido pedido) {
        this.id = pedido.getId();
        this.status = pedido.getStatus().name();
    }

    public long getId() { return id; }
    public String getStatus() { return status; }
}
