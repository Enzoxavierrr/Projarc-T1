package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Component
public class PedidoRepositoryJDBC implements PedidoRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        String sql = "INSERT INTO pedidos (cliente_cpf, data_criacao, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, pedido.getCliente().getCpf());
            ps.setObject(2, LocalDateTime.now());
            ps.setObject(3, pedido.getDataHoraPagamento());
            ps.setString(4, pedido.getStatus().name());
            ps.setDouble(5, pedido.getValor());
            ps.setDouble(6, pedido.getImpostos());
            ps.setDouble(7, pedido.getDesconto());
            ps.setDouble(8, pedido.getValorCobrado());
            return ps;
        }, keyHolder);

        long idGerado = keyHolder.getKey().longValue();

        for (ItemPedido item : pedido.getItens()) {
            jdbcTemplate.update(
                    "INSERT INTO itens_pedido (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)",
                    idGerado, item.getItem().getId(), item.getQuantidade());
        }
        return pedido;
    }

    @Override
    public int contarPedidosDoClienteDesde(Cliente cliente, LocalDateTime desde) {
        String sql = "SELECT COUNT(*) FROM pedidos WHERE cliente_cpf = ? AND data_criacao >= ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, cliente.getCpf(), desde);
    }

}
