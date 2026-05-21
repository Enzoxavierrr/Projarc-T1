package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests;

public record AutenticarResponse (String cpf) {

}


//record é uma maneira mais limpa de criar classes de dodos simples, assim não precisa escrever construtor + getters 
//faz tudo automaticamente