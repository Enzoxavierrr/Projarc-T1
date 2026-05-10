package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Service
public class EstoqueServiceFake implements IEstoqueService {

    @Override
    public List<ItemPedido> verificarEstoque(List<ItemPedido> itens) {
        List<ItemPedido> itensIndisponiveis = new ArrayList<>();

        for (ItemPedido item : itens) {
            if (item.getItem().getId() == 1L) {
                itensIndisponiveis.add(item);
            }
        }

        return itensIndisponiveis;
    }

}
