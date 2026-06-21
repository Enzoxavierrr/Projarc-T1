package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos.ICalculoImposto;

@Service
public class ImpostoService implements IImpostoService {

    private final ICalculoImposto calculoImposto;

    public ImpostoService(ICalculoImposto calculoImposto) {
        this.calculoImposto = calculoImposto;
    }

    @Override
    public double calcularImposto(double subtotal) {
        return calculoImposto.calcular(subtotal);
    }

}
