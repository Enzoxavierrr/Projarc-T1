package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface IDescontoService {
    double calcularDesconto(Cliente cliente, double subtotal);
}