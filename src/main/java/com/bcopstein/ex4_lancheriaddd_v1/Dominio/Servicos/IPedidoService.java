package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Optional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

public interface IPedidoService {
    Pedido processarPedido(Pedido pedido);
    void cancelar(long id);
    Optional<Pedido> buscarPorId(long id);
}
