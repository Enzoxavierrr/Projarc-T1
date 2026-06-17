package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

public interface IEstoqueService {
    List<ItemPedido> verificarEstoque(List<ItemPedido> itens);
    void darBaixaEstoque(List<ItemPedido> itens);
}
