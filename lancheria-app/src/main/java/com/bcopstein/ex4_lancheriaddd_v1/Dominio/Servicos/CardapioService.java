package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class CardapioService {
    private CardapioRepository cardapioRepository;

    public CardapioService(CardapioRepository cardapioRepository){
        this.cardapioRepository = cardapioRepository;
    }

    public Cardapio recuperaCardapio(long Id){
        return cardapioRepository.recuperaPorId(Id);
    }

    public List<CabecalhoCardapio> recuperaListaDeCardapios(){
        return cardapioRepository.cardapiosDisponiveis();
    }

    public List<Produto> recuperaSugestoesDoChef(){
        return cardapioRepository.indicacoesDoChef();
    }

    public Cardapio recuperaCardapioCorrente(){
        long id = cardapioRepository.recuperaIdCardapioCorrente();
        return cardapioRepository.recuperaPorId(id);
    } 

    public void definirCardapioCorrente(long id) {
        if (cardapioRepository.recuperaPorId(id) == null) {
            throw new IllegalArgumentException("Cardapio nao encontrado com id: " + id);
        }
        cardapioRepository.definirIdCardapioCorrente(id);
    }
}
