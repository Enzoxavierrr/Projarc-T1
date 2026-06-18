package com.projarc.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.projarc.estoque.domain.Ingrediente;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
}
