package com.projarc.estoque.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredienteQtdDTO {
    private Long ingredienteId;
    private int quantidade;
}
