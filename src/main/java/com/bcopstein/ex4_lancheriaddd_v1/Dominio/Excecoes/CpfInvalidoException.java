package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException(String cpf) {
        super("CPF '" + cpf + "' inválido. Informe exatamente 11 dígitos numéricos.");
    }
}
