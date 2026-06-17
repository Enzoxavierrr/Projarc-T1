package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;


@Service
public class EstoqueServiceMicroServico implements IEstoqueService {

    private final RestTemplate restTemplate;

    public EstoqueServiceMicroServico(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ItemPedido> verificarEstoque(List<ItemPedido> itens) {
        Map<Long, Integer> ingredientesNecessarios = new HashMap<>();

        for (ItemPedido itemPedido : itens) {
            Produto produto = itemPedido.getItem();
            Receita receita = produto.getReceita();
            
            for (Ingrediente ingrediente : receita.getIngredientes()) {
                long ingredienteId = ingrediente.getId();
                int qtdNecessaria = itemPedido.getQuantidade();
                
                ingredientesNecessarios.put(
                    ingredienteId, 
                    ingredientesNecessarios.getOrDefault(ingredienteId, 0) + qtdNecessaria
                );
            }
        }

        List<IngredienteQtd> payload = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : ingredientesNecessarios.entrySet()) {
            payload.add(new IngredienteQtd(entry.getKey(), entry.getValue()));
        }

        IngredienteQtd[] faltantes = restTemplate.postForObject(
            "http://estoque/estoque/verificar", 
            payload, 
            IngredienteQtd[].class
        );

        Set<Long> idsFaltantes = new HashSet<>();
        if (faltantes != null) {
            for (IngredienteQtd f : faltantes) {
                idsFaltantes.add(f.ingredienteId());
            }
        }
        if (idsFaltantes.isEmpty()) {
            return new ArrayList<>(); 
        }
        List<ItemPedido> itensIndisponiveis = new ArrayList<>();
        for (ItemPedido itemPedido : itens) {
            Produto produto = itemPedido.getItem();
            Receita receita = produto.getReceita();
            
            for (Ingrediente ingrediente : receita.getIngredientes()) {
                if (idsFaltantes.contains(ingrediente.getId())) {
                    itensIndisponiveis.add(itemPedido);
                    break; 
                }
            }
        }
        return itensIndisponiveis;
    }

    public record IngredienteQtd(Long ingredienteId, int quantidade) {

    }
}