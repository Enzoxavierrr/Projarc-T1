package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos;

public class ImpostoLei5678 implements ICalculoImposto {
    @Override
    public double calcular(double valor) {
        if (valor <= 100) {
            return valor * 0.08;
        }
        return valor * 0.12;
    }

    @Override
    public String getId() {
        return "lei_5678";
    }
}
