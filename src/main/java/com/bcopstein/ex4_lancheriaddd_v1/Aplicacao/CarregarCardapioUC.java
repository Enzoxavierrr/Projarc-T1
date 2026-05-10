package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.CardapioService;

//User Case = UC - Simula o carregamento do cardápio corrente, ou seja, aquele que está disponível para os clientes no momento.
@Component // Anotação para indicar que esta classe é um componente gerenciado pelo Spring, permitindo a injeção de dependências
public class CarregarCardapioUC {
    private CardapioService cardapioService;

    @Autowired // Anotação para indicar que o Spring deve injetar a dependência do CardapioService
    public CarregarCardapioUC(CardapioService cardapioService){
        this.cardapioService = cardapioService;
    }   

    public CardapioResponse run(){
        Cardapio cardapio = cardapioService.recuperaCardapioCorrente();
        List<Produto> sugestoes = cardapioService.recuperaSugestoesDoChef();
        return new CardapioResponse(cardapio, sugestoes);
    }
}
