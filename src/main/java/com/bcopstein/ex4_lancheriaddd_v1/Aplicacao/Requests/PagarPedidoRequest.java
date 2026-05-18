package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

public class PagarPedidoRequest {
    private String cpf;

    public PagarPedidoRequest(String cpf) {
        this.cpf = cpf;
    }

    public String getCpf() { return cpf; }
}