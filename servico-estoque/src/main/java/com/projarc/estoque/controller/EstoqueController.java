package com.projarc.estoque.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projarc.estoque.dto.IngredienteQtdDTO;
import com.projarc.estoque.service.EstoqueService;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/verificar")
    public ResponseEntity<List<IngredienteQtdDTO>> verificarEstoque(
            @RequestBody List<IngredienteQtdDTO> itensNecessarios) {
        List<IngredienteQtdDTO> faltantes = estoqueService.verificarEstoque(itensNecessarios);
        return ResponseEntity.ok(faltantes);
    }

    @PostMapping("/baixar")
    public ResponseEntity<Void> baixarEstoque(@RequestBody List<IngredienteQtdDTO> itensParaBaixar) {
        estoqueService.baixarEstoque(itensParaBaixar);
        return ResponseEntity.ok().build();
    }
}
