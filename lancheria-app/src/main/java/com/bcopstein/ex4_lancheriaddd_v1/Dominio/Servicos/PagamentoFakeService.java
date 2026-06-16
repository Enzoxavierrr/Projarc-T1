package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class PagamentoFakeService implements IPagamentoService {
    @Override
    public boolean pagar(Pedido pedido) {
        return true;
    }
}
