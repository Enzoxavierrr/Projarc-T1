package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.CabecalhoCardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao.Presenters.CardapioPresenter;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaListaCardapiosUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperarCardapioUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CardapioResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import org.springframework.web.bind.annotation.RequestParam;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CarregarCardapioUC;



@RestController
@RequestMapping("/cardapio")
public class CardapioController {
    private RecuperarCardapioUC recuperaCardapioUC;
    private RecuperaListaCardapiosUC recuperaListaCardapioUC;
    private CarregarCardapioUC carregarCardapioUC;

    public CardapioController(RecuperarCardapioUC recuperaCardapioUC,
                              RecuperaListaCardapiosUC recuperaListaCardapioUC,
                            CarregarCardapioUC carregarCardapioUC) {
        this.recuperaCardapioUC = recuperaCardapioUC;
        this.recuperaListaCardapioUC = recuperaListaCardapioUC;
        this.carregarCardapioUC = carregarCardapioUC;
    }

    @GetMapping("/{id}")
    @CrossOrigin("*")
    public CardapioPresenter recuperaCardapio(@PathVariable(value="id")long id){
        CardapioResponse cardapioResponse = recuperaCardapioUC.run(id);
        Set<Long> conjIdSugestoes = new HashSet<>(cardapioResponse.getSugestoesDoChef().stream()
            .map(produto->produto.getId())
            .toList());
        CardapioPresenter cardapioPresenter = new CardapioPresenter(cardapioResponse.getCardapio().getCabecalhoCardapio().titulo());
        for(Produto produto:cardapioResponse.getCardapio().getProdutos()){
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            cardapioPresenter.insereItem(produto.getId(), produto.getDescricao(), produto.getPreco(), sugestao);
        }
        return cardapioPresenter;
    }

    @GetMapping("/lista")
    @CrossOrigin("*")
    public List<CabecalhoCardapioPresenter> recuperaListaCardapios(){
         List<CabecalhoCardapioPresenter> lstCardapios = 
            recuperaListaCardapioUC.run().cabecalhos().stream()
            .map(cabCar -> new CabecalhoCardapioPresenter(cabCar.id(),cabCar.titulo()))
            .toList();
         return lstCardapios;
    }

    @GetMapping("/corrente")
    @CrossOrigin("*") 
    //CrossOrigin é necessário para permitir que o frontend acesse 
    // este endpoint mesmo que esteja em um domínio diferente, 
    // o que é comum durante o desenvolvimento
    public CardapioPresenter carregarCardapio() {
        CardapioResponse cardapioResponse = carregarCardapioUC.run();      
        Set<Long> conjIdSugestoes = new HashSet<>(cardapioResponse.getSugestoesDoChef()
            .stream()
            .map(p -> p.getId())
            .toList());     
        
        CardapioPresenter presenter = new CardapioPresenter(
            cardapioResponse.getCardapio().getCabecalhoCardapio().titulo()
        );

        for (Produto produto : cardapioResponse.getCardapio().getProdutos()) {
            boolean sugestao = conjIdSugestoes.contains(produto.getId());
            presenter.insereItem(
                produto.getId(),
                produto.getDescricao(),
                produto.getPreco(),
                sugestao
            );
        }

        return presenter;
    }
    
}
