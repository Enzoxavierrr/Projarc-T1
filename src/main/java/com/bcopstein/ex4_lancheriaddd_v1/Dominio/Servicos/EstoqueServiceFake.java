package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Service
public class EstoqueServiceFake implements IEstoqueService {
    @Override
    public List<ItemPedido> verificarEstoque(List<ItemPedido> itens) {
        return List.of();
    }

}
