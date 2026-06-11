package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

public record RegistrarClienteRequest(
    String cpf,
    String nome,
    String email,
    String senha,
    String celular,
    String endereco
)
{}
