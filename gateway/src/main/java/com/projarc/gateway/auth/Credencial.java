package com.projarc.gateway.auth;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("credenciais")
public class Credencial implements Persistable<String> {

    @Id
    private String cpf;
    private String email;
    private String senha;

    @Transient
    private boolean isNew = true;

    public Credencial() {}

    public Credencial(String cpf, String email, String senha) {
        this.cpf   = cpf;
        this.email = email;
        this.senha = senha;
    }

    @Override
    public String getId() { return cpf; }

    @Override
    public boolean isNew() { return isNew; }

    public String getCpf()   { return cpf; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}
