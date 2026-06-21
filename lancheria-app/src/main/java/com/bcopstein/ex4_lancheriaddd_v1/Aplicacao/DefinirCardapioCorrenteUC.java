package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;

@Component
public class DefinirCardapioCorrenteUC {
    private final CardapioService cardapioService;

    public DefinirCardapioCorrenteUC(CardapioService cardapioService) {
        this.cardapioService = cardapioService;
    }

    public void run(long id) {
        cardapioService.definirCardapioCorrente(id);
    }
}
