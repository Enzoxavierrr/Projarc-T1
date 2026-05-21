package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.stereotype.Component;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Component
public class ClienteRepositoryJDBC implements ClienteRepository {
private JdbcTemplate jdbcTemplate;

public ClienteRepositoryJDBC(JdbcTemplate jdbcTemplate){
    this.jdbcTemplate = jdbcTemplate;
}

Override
public void salvar(Cliente cliente) {
    String sql = "INSERT INTO clientes (cpf, nome, celular, endereco, email, senha) VALUES (?, ?, ?, ?, ?, ?)";
    jdbcTemplate.update(sql,
        cliente.getCpf(),
        cliente.getNome(),
        cliente.getCelular(),
        cliente.getEndereco(),
        cliente.getEmail(),
        cliente.getSenha()
    );
}
@Override
public Cliente buscarPorEmail(String email) {
    String sql = "SELECT cpf, nome, celular, endereco, email, senha FROM clientes WHERE email = ?";
    List<Cliente> clientes = jdbcTemplate.query(
        sql,
        ps -> ps.setString(1, email),
        (rs, rowNum) -> new Cliente(
            rs.getString("cpf"),
            rs.getString("nome"),
            rs.getString("celular"),
            rs.getString("endereco"),
            rs.getString("email"),
            rs.getString("senha")
        )
    );
    return clientes.isEmpty() ? null : clientes.getFirst();
}

@Override
public Cliente buscarPorCpf(String cpf) {
    String sql = "SELECT cpf, nome, celular, endereco, email FROM clientes WHERE cpf = ?";
    List<Cliente> clientes = this.jdbcTemplate.query(
        sql,
        ps -> ps.setString(1, cpf),
        (rs, rowNum) -> new Cliente(
            rs.getString("cpf"), 
            rs.getString("nome"),
            rs.getString("celular"),
            rs.getString("endereco"),
            rs.getString("email")
        )
    );
    return clientes.isEmpty() ? null : clientes.getFirst();

}

}
