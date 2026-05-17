package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public class StatusPedidoResponse {
    private long id;
    private String status;

    public StatusPedidoResponse(long id, Pedido.Status status) {
        this.id = id;
        this.status = status.name();
    }

    public long getId() { return id; }
    public String getStatus() { return status; }
}
