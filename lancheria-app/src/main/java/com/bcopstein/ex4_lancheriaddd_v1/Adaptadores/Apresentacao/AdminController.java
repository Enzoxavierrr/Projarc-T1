package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DefinirCardapioCorrenteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos.DescontoContext;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final DescontoContext descontoContext;
    private final DefinirCardapioCorrenteUC definirCardapioCorrenteUC;

    public AdminController(DescontoContext descontoContext,
                           DefinirCardapioCorrenteUC definirCardapioCorrenteUC) {
        this.descontoContext = descontoContext;
        this.definirCardapioCorrenteUC = definirCardapioCorrenteUC;
    }

    @GetMapping("/desconto")
    @CrossOrigin("*")
    public Map<String, Object> listarDescontos() {
        return Map.of(
            "disponiveis", descontoContext.listarCodigosDisponiveis(),
            "ativo", descontoContext.getCodigoAtivo());
    }

    @PutMapping("/desconto/{codigo}")
    @CrossOrigin("*")
    public ResponseEntity<Void> definirEstrategia(@PathVariable String codigo) {
        descontoContext.definirEstrategiaAtiva(codigo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cardapio/{id}")
    @CrossOrigin("*")
    public ResponseEntity<Void> definirCardapioCorrente(@PathVariable long id) {
        definirCardapioCorrenteUC.run(id);
        return ResponseEntity.noContent().build();
    }
}
