package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Email ou senha inválidos.");
    }
}
