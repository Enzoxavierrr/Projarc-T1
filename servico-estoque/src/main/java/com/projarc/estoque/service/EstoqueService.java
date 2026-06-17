package com.projarc.estoque.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projarc.estoque.domain.ItemEstoque;
import com.projarc.estoque.dto.IngredienteQtdDTO;
import com.projarc.estoque.repository.ItemEstoqueRepository;

@Service
public class EstoqueService {

    private final ItemEstoqueRepository itemEstoqueRepository;

    public EstoqueService(ItemEstoqueRepository itemEstoqueRepository) {
        this.itemEstoqueRepository = itemEstoqueRepository;
    }

    /**
     * Verifica quais ingredientes estão faltando no estoque.
     * Recebe uma lista de ingredientes e quantidades necessárias,
     * e retorna a lista de ingredientes que não têm estoque suficiente.
     */
    public List<IngredienteQtdDTO> verificarEstoque(List<IngredienteQtdDTO> itensNecessarios) {
        List<IngredienteQtdDTO> ingredientesFaltantes = new ArrayList<>();

        for (IngredienteQtdDTO item : itensNecessarios) {
            Optional<ItemEstoque> optItemEstoque = itemEstoqueRepository.findByIngredienteId(item.getIngredienteId());

            if (optItemEstoque.isEmpty()) {
                // Se não existe o item no estoque, ele está totalmente faltando
                ingredientesFaltantes.add(new IngredienteQtdDTO(item.getIngredienteId(), item.getQuantidade()));
            } else {
                ItemEstoque itemEstoque = optItemEstoque.get();
                if (itemEstoque.getQuantidade() < item.getQuantidade()) {
                    // Se a quantidade em estoque é menor que a necessária
                    int faltante = item.getQuantidade() - itemEstoque.getQuantidade();
                    ingredientesFaltantes.add(new IngredienteQtdDTO(item.getIngredienteId(), faltante));
                }
            }
        }

        return ingredientesFaltantes;
    }

    /**
     * Desconta as quantidades de ingredientes do estoque.
     */
    @Transactional
    public void baixarEstoque(List<IngredienteQtdDTO> itensParaBaixar) {
        for (IngredienteQtdDTO item : itensParaBaixar) {
            Optional<ItemEstoque> optItemEstoque = itemEstoqueRepository.findByIngredienteId(item.getIngredienteId());

            if (optItemEstoque.isPresent()) {
                ItemEstoque itemEstoque = optItemEstoque.get();
                int novaQuantidade = itemEstoque.getQuantidade() - item.getQuantidade();
                if (novaQuantidade < 0) {
                    novaQuantidade = 0; // Evita estoque negativo
                }
                itemEstoque.setQuantidade(novaQuantidade);
                itemEstoqueRepository.save(itemEstoque);
            }
        }
    }
}
