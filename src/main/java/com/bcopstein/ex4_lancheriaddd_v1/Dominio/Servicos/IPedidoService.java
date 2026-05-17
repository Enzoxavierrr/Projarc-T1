package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ResultadoPedido;

public interface IPedidoService {
ResultadoPedido processarPedido(Pedido pedido);
void cancelar(long id, String cpf);
}
