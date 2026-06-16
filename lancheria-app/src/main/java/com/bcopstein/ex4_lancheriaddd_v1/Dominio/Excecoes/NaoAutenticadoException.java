package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

public class NaoAutenticadoException extends RuntimeException {
    public NaoAutenticadoException() {
        super("Token de autenticação ausente ou inválido.");
    }
}
