package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Component
public class PromocaoVerao implements ICalculoDesconto {
    @Override
    public double calcular(double valorItem, Cliente cliente) {
        return valorItem * 0.15;
    }

    @Override
    public String getCodigo() {
        return "PromocaoVerao";
    }
}
