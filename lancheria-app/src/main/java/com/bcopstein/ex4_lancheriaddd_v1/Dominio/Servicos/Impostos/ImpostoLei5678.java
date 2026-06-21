package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos;

public class ImpostoLei5678 implements ICalculoImposto {
    @Override
    public double calcular(double valor) {
        if (valor <= 10_000) { // R$ 100,00 em centavos
            return valor * 0.08;
        }
        return valor * 0.12;
    }

    @Override
    public String getId() {
        return "lei_5678";
    }
}
