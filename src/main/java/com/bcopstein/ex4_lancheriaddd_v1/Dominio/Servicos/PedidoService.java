package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoEncontradoException;

@Service
public class PedidoService {
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

    public ResultadoPedido processarPedido(Pedido pedido) {
        List<ItemPedido> itensIndisponiveis = estoqueService.verificarEstoque(pedido.getItens());
        if (!itensIndisponiveis.isEmpty()) {
            pedido.reprovar();
            return new ResultadoPedido(pedido, itensIndisponiveis);
        }

        double subtotal = pedido.calcularSubtotal();
        double desconto = descontoService.calcularDesconto(pedido.getCliente(), subtotal);
        double imposto = impostoService.calcularImposto(subtotal);

        pedido.aprovar(desconto, imposto);

        return new ResultadoPedido(pedidoRepository.salvar(pedido), List.of());
    }

    public Pedido.Status buscaStatusPorId(long id) {
        return pedidoRepository.buscaStatusPorId(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }

}
