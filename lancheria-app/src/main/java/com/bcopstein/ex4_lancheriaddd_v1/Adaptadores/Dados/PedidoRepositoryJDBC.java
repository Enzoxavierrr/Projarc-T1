package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    private static final org.springframework.jdbc.core.RowMapper<Pedido> PEDIDO_ROW_MAPPER =
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
        );

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

        Number key = keyHolder.getKey();
        if (key == null) throw new IllegalStateException("Nenhum ID gerado pelo banco");
        long idGerado = key.longValue();
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
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cliente.getCpf(), desde);
        return count != null ? count : 0;
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
    } // aqui o repo faz apenas a query no banco. Repara que ele retorna um optional - se não achar nd, devolve um empty() e o serviço de dominio trata a excecao.


    @Override
    public void atualizarStatus(long id, Pedido.Status status) {
        int linhasAfetadas;
        if (status == Pedido.Status.ENTREGUE) {
            linhasAfetadas = jdbcTemplate.update(
                "UPDATE pedidos SET status = ?, data_entrega = ? WHERE id = ?",
                status.name(), LocalDateTime.now(), id);
        } else {
            linhasAfetadas = jdbcTemplate.update(
                "UPDATE pedidos SET status = ? WHERE id = ?",
                status.name(), id);
        }
        if (linhasAfetadas == 0) {
            throw new PedidoNaoEncontradoException(id);
        }
    }

    @Override
    public Optional<Pedido> buscarResumoPorId(long id) {
        String sql = "SELECT p.id, p.status, p.valor, p.impostos, p.desconto, p.valor_cobrado, " +
                     "p.data_hora_pagamento, p.endereco_entrega, " +
                     "c.cpf, c.nome, c.celular, c.endereco, c.email " +
                     "FROM pedidos p JOIN clientes c ON p.cliente_cpf = c.cpf " +
                     "WHERE p.id = ?";
        List<Pedido> resultado = jdbcTemplate.query(sql, ps -> ps.setLong(1, id), PEDIDO_ROW_MAPPER);
        if (resultado.isEmpty()) {
            return Optional.empty();
        }
        Pedido pedidoSemItens = resultado.getFirst();

        // Carrega os itens do pedido com suas respectivas receitas e ingredientes
        String itemsSql = "SELECT ip.id as item_id, ip.quantidade, ip.produto_id, p.descricao as prod_desc, p.preco, p.disponivel, pr.receita_id, r.titulo as rec_titulo " +
                          "FROM itens_pedido ip " +
                          "JOIN produtos p ON ip.produto_id = p.id " +
                          "JOIN produto_receita pr ON p.id = pr.produto_id " +
                          "JOIN receitas r ON pr.receita_id = r.id " +
                          "WHERE ip.pedido_id = ?";

        List<ItemPedido> itens = jdbcTemplate.query(itemsSql, ps -> ps.setLong(1, id), (rs, rowNum) -> {
            long produtoId = rs.getLong("produto_id");
            String prodDesc = rs.getString("prod_desc");
            int preco = rs.getInt("preco");
            boolean disponivel = rs.getBoolean("disponivel");
            long receitaId = rs.getLong("receita_id");
            String recTitulo = rs.getString("rec_titulo");

            // Carrega os ingredientes para esta receita
            String ingredientsSql = "SELECT i.id, i.descricao " +
                                    "FROM ingredientes i " +
                                    "JOIN receita_ingrediente ri ON i.id = ri.ingrediente_id " +
                                    "WHERE ri.receita_id = ?";
            List<com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente> ingredientes = jdbcTemplate.query(
                ingredientsSql,
                ps -> ps.setLong(1, receitaId),
                (rsIng, rNum) -> new com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente(
                    rsIng.getLong("id"),
                    rsIng.getString("descricao")
                )
            );

            com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita receita = 
                new com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita(receitaId, recTitulo, ingredientes);

            com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto produto = 
                new com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto(produtoId, prodDesc, receita, preco, disponivel);

            return new ItemPedido(produto, rs.getInt("quantidade"));
        });

        Pedido pedidoCompleto = new Pedido(
            pedidoSemItens.getId(),
            pedidoSemItens.getCliente(),
            pedidoSemItens.getDataHoraPagamento(),
            itens,
            pedidoSemItens.getStatus(),
            pedidoSemItens.getValor(),
            pedidoSemItens.getImpostos(),
            pedidoSemItens.getDesconto(),
            pedidoSemItens.getValorCobrado(),
            pedidoSemItens.getEnderecoEntrega()
        );

        return Optional.of(pedidoCompleto);
    }

    @Override
    public List<Pedido> listarEntreguesEntreDatas(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT p.id, p.status, p.valor, p.impostos, p.desconto, p.valor_cobrado, " +
                     "p.data_hora_pagamento, p.endereco_entrega, " +
                     "c.cpf, c.nome, c.celular, c.endereco, c.email " +
                     "FROM pedidos p JOIN clientes c ON p.cliente_cpf = c.cpf " +
                     "WHERE p.status = 'ENTREGUE' " +
                     "AND p.data_entrega >= ? AND p.data_entrega < ?";
        return jdbcTemplate.query(
            sql,
            ps -> {
                ps.setObject(1, inicio.atStartOfDay());
                ps.setObject(2, fim.plusDays(1).atStartOfDay());
            },
            PEDIDO_ROW_MAPPER
        );
    }

    @Override
    public List<Pedido> listarEntreguesDoClienteEntreDatas(String cpf, LocalDate inicio, LocalDate fim) {
        String sql = "SELECT p.id, p.status, p.valor, p.impostos, p.desconto, p.valor_cobrado, " +
                     "p.data_hora_pagamento, p.endereco_entrega, " +
                     "c.cpf, c.nome, c.celular, c.endereco, c.email " +
                     "FROM pedidos p JOIN clientes c ON p.cliente_cpf = c.cpf " +
                     "WHERE p.status = 'ENTREGUE' AND c.cpf = ? " +
                     "AND p.data_entrega >= ? AND p.data_entrega < ?";
        return jdbcTemplate.query(
            sql,
            ps -> {
                ps.setString(1, cpf);
                ps.setObject(2, inicio.atStartOfDay());
                ps.setObject(3, fim.plusDays(1).atStartOfDay());
            },
            PEDIDO_ROW_MAPPER
        );
    }

}
