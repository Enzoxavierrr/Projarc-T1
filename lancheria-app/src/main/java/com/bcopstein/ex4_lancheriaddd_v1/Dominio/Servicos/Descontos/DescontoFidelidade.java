package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Component
public class DescontoFidelidade implements ICalculoDesconto {
    private final PedidoRepository pedidoRepository;

    @Autowired
    public DescontoFidelidade(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public double calcular(double valorItem, Cliente cliente) {
        int pedidosRecentes = pedidoRepository.contarPedidosDoClienteDesde(
            cliente,
            LocalDateTime.now().minusDays(20));
        if (pedidosRecentes > 3) {
            return valorItem * 0.07;
        }
        return 0;
    }

    @Override
    public String getCodigo() {
        return "DescontoFidelidade";
    }
}
