package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Service
public class DescontoService implements IDescontoService {
    private PedidoRepository pedidoRepository;


    @Autowired
        public DescontoService(PedidoRepository pedidoRepository){
            this.pedidoRepository = pedidoRepository;
        }
    //Logica: se o cliente fez mais de 3 pedidos nos ultimos 20 dias
    //aplica 7% de desconto no subtotal
    @Override
    public double calcularDesconto(Cliente cliente, double subtotal) {
       
        int pedidosRecentes = pedidoRepository.contarPedidosDoClienteDesde(
            cliente, 
            LocalDateTime.now().minusDays(20)); //conta os pedidos do cliente nos ultimos 20 dias
        if(pedidosRecentes > 3){
            return subtotal * 0.07;
        }
        return 0; // sem desconto
    }
}
