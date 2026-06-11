package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

// record é uma maneira mais limpa de criar classes de dados simples,
// assim não precisa escrever construtor + getters — faz tudo automaticamente
public record AutenticarResponse(String cpf, String mensagem) {}
