package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Service
public class ProdutoService {
    private ProdutosRepository produtosRepository;

    public ProdutoService(ProdutosRepository produtosRepository) {
        this.produtosRepository = produtosRepository;
    }

    public Produto recuperaProdutoPorId(long id) {
        return produtosRepository.recuperaProdutoPorid(id);
    }
}
