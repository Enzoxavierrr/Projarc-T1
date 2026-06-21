package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos.DescontoContext;

@Service
public class DescontoService implements IDescontoService {
    private final DescontoContext descontoContext;

    public DescontoService(DescontoContext descontoContext) {
        this.descontoContext = descontoContext;
    }

    @Override
    public double calcularDesconto(Cliente cliente, double subtotal) {
        return descontoContext.calcular(subtotal, cliente);
    }
}
