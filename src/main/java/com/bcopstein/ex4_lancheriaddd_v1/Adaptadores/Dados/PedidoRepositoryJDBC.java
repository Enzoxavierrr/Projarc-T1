package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoEncontradoException;

@Component
public class PedidoRepositoryJDBC implements PedidoRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PedidoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Pedido salvar(Pedido pedido) {
        String sql = "INSERT INTO pedidos (cliente_cpf, data_criacao, data_hora_pagamento, status, valor, impostos, desconto, valor_cobrado, endereco_entrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.setString(9, pedido.getEnderecoEntrega());
            return ps;
        }, keyHolder);

        long idGerado = keyHolder.getKey().longValue();
        pedido.setId(idGerado);

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

    @Override
    public Optional<Pedido.Status> buscaStatusPorId(long id) {
        String sql = "SELECT status FROM pedidos WHERE id = ?";
        return jdbcTemplate.query(
                sql,
                ps -> ps.setLong(1, id),
                rs -> {
                    if (!rs.next()) return Optional.<Pedido.Status>empty();
                    return Optional.of(Pedido.Status.valueOf(rs.getString("status")));
                });
    }

    @Override
    public Optional<Pedido> buscarResumoPorId(long id) {
        String sql = "SELECT p.id, p.status, p.valor, p.impostos, p.desconto, p.valor_cobrado, " +
                     "p.data_hora_pagamento, p.endereco_entrega, " +
                     "c.cpf, c.nome, c.celular, c.endereco, c.email " +
                     "FROM pedidos p JOIN clientes c ON p.cliente_cpf = c.cpf " +
                     "WHERE p.id = ?";
        List<Pedido> resultado = jdbcTemplate.query(
            sql,
            ps -> ps.setLong(1, id),
            (rs, rowNum) -> new Pedido(
                rs.getLong("id"),
                new Cliente(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("celular"),
                    rs.getString("endereco"),
                    rs.getString("email")
                ),
                rs.getObject("data_hora_pagamento", LocalDateTime.class),
                List.of(),
                Pedido.Status.valueOf(rs.getString("status")),
                rs.getDouble("valor"),
                rs.getDouble("impostos"),
                rs.getDouble("desconto"),
                rs.getDouble("valor_cobrado"),
                rs.getString("endereco_entrega")
            )
        );
        return resultado.isEmpty() ? Optional.empty() : Optional.of(resultado.getFirst());
    }

    @Override
    public void atualizarStatus(long id, Pedido.Status status) {
        int linhasAfetadas = jdbcTemplate.update("UPDATE pedidos SET status = ? WHERE id = ?", status.name(), id);
        if (linhasAfetadas == 0) {
            throw new PedidoNaoEncontradoException(id);
        }
    }

}
