package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

public class PedidoListagemResponse {
    private long id;
    private String clienteCpf;
    private String clienteNome;
    private String status;
    private double valorCobrado;
    private String enderecoEntrega;

    public PedidoListagemResponse(long id, String clienteCpf, String clienteNome,
                                   String status, double valorCobrado, String enderecoEntrega) {
        this.id = id;
        this.clienteCpf = clienteCpf;
        this.clienteNome = clienteNome;
        this.status = status;
        this.valorCobrado = valorCobrado;
        this.enderecoEntrega = enderecoEntrega;
    }

    public long getId() { return id; }
    public String getClienteCpf() { return clienteCpf; }
    public String getClienteNome() { return clienteNome; }
    public String getStatus() { return status; }
    public double getValorCobrado() { return valorCobrado; }
    public String getEnderecoEntrega() { return enderecoEntrega; }
}
