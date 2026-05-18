package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;

@Service
public class PedidoService implements IPedidoService {
    private PedidoRepository pedidoRepository;
    private IDescontoService descontoService;
    private IImpostoService impostoService;
    private IEstoqueService estoqueService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, IDescontoService descontoService,
            IImpostoService impostoService, IEstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.descontoService = descontoService;
        this.impostoService = impostoService;
        this.estoqueService = estoqueService;
    }

    @Override
    public Pedido processarPedido(Pedido pedido) {
        List<ItemPedido> itensIndisponiveis = estoqueService.verificarEstoque(pedido.getItens());
        if (!itensIndisponiveis.isEmpty()) {
            pedido.setStatus(Pedido.Status.NOVO);
            return pedido;
        }
        double subtotal = pedido.getItens().stream()
            .mapToDouble(item -> item.getItem().getPreco() * item.getQuantidade())
            .sum();

        double desconto = descontoService.calcularDesconto(pedido.getCliente(), subtotal);
        double imposto = impostoService.calcularImposto(subtotal);
        double valorCobrado = subtotal - desconto + imposto;

        pedido.setStatus(Pedido.Status.APROVADO);
        pedido.setValor(subtotal);
        pedido.setImpostos(imposto);
        pedido.setDesconto(desconto);
        pedido.setValorCobrado(valorCobrado);

        return pedidoRepository.salvar(pedido);
    }

    @Override
    public void cancelar(long id) {
        pedidoRepository.atualizarStatus(id, Pedido.Status.CANCELADO);
    }

    @Override
    public Optional<Pedido> buscarPorId(long id) {
        return pedidoRepository.buscarPorId(id);
    }
}
