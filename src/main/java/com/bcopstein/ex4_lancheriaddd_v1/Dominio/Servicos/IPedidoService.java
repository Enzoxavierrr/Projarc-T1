package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Optional;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ResultadoPedido;

public interface IPedidoService {
    ResultadoPedido processarPedido(Pedido pedido);
    Optional<Pedido> buscarPorId(long id);
    void cancelar(long id, String cpf);
}
