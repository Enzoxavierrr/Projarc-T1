package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

public class ClienteJaCadastradoException extends RuntimeException {
    public ClienteJaCadastradoException(String campo, String valor) {
        super(campo + " '" + valor + "' já está cadastrado no sistema.");
    }
}
