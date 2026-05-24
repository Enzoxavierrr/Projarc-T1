package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    int contarPedidosDoClienteDesde(Cliente cliente, LocalDateTime desde);
    Optional<Pedido.Status> buscaStatusPorId(long id);
    Optional<Pedido> buscarResumoPorId(long id);
    void atualizarStatus(long id, Pedido.Status status);
    List<Pedido> listarEntreguesEntreDatas(LocalDate inicio, LocalDate fim);
}
