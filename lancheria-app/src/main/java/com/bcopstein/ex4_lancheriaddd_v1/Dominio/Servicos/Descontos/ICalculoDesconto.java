package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ICalculoDesconto {
    double calcular(double valorItem, Cliente cliente);
    String getCodigo();
}
