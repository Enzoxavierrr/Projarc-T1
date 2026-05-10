package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;
    private IDescontoService descontoService;
    private IImpostoService impostoService;
    private IEstoqueService estoqueService;


    @Autowired
        public PedidoService(PedidoRepository pedidoRepository, IDescontoService descontoService,
            IImpostoService impostoService, IEstoqueService estoqueService){
            this.pedidoRepository = pedidoRepository;
            this.descontoService = descontoService;
            this.impostoService = impostoService;
            this.estoqueService = estoqueService;  
        }

        public Pedido processarPedido(Pedido pedido){
            //verificar estoque
            List<ItemPedido> itensIndisponiveis = estoqueService.verificarEstoque(pedido.getItens());
            if(!itensIndisponiveis.isEmpty()){
                pedido.setStatus(Pedido.Status.NOVO);
                return pedido; // retorna sem salvar, UC que trata
            }
            //calcular desconto
            double subtotal = pedido.getItens().stream()
                .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
                .sum(); // calcula o subtotal do pedido, o sum é para somar o valor de cada item (preco * quantidade)

            double desconto = descontoService.calcularDesconto(pedido.getCliente(), subtotal);
            //calcular imposto
            double imposto = impostoService.calcularImposto(subtotal);
            double valorCobrado = subtotal - desconto + imposto;


            //atualizar pedido
            pedido.setStatus(Pedido.Status.APROVADO);
            pedido.setValor(subtotal);
            pedido.setImpostos(imposto);
            pedido.setDesconto(desconto);
            pedido.setValorCobrado(valorCobrado);

            //salvar pedido
            return pedidoRepository.salvar(pedido);
        }

}
