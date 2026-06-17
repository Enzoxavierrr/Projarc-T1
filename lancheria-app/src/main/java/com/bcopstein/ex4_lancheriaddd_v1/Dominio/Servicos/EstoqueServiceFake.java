package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
//Nao sera mais usada pois agora tem o microservico de estoque
//@Service
public class EstoqueServiceFake implements IEstoqueService {
    @Override
    public List<ItemPedido> verificarEstoque(List<ItemPedido> itens) {
        return itens.stream()
                .filter(item -> item.getItem().getId() == 1)
                .toList();
    }

}
