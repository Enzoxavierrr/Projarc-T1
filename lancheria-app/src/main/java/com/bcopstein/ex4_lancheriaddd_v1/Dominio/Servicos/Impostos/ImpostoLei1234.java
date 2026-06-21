package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos;

public class ImpostoLei1234 implements ICalculoImposto {
    @Override
    public double calcular(double valor) {
        return valor * 0.10;
    }

    @Override
    public String getId() {
        return "lei_1234";
    }
}
