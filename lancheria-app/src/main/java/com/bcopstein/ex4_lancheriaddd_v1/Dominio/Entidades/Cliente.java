package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

public class Cliente {
    private String cpf;
    private String nome;
    private String email;
    private String senha;
    private String celular;
    private String endereco;
    
    

    public Cliente(String cpf, String nome, String celular, String endereco, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.celular = celular;
        this.endereco = endereco;
        this.email = email;
    }

    public Cliente(String cpf, String nome, String celular, String endereco, String email, String senha) {
        this(cpf, nome, celular, endereco, email);
        this.senha = senha;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getCelular() { return celular; }
    public String getEndereco() { return endereco; }
    
}
