package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos.DescontoContext;

@RestController
@RequestMapping("/admin/desconto")
public class AdminController {
    private final DescontoContext descontoContext;

    public AdminController(DescontoContext descontoContext) {
        this.descontoContext = descontoContext;
    }

    @GetMapping("")
    @CrossOrigin("*")
    public Map<String, Object> listar() {
        return Map.of(
            "disponiveis", descontoContext.listarCodigosDisponiveis(),
            "ativo", descontoContext.getCodigoAtivo());
    }

    @PutMapping("/{codigo}")
    @CrossOrigin("*")
    public ResponseEntity<Void> definirEstrategia(@PathVariable String codigo) {
        descontoContext.definirEstrategiaAtiva(codigo);
        return ResponseEntity.noContent().build();
    }
}
