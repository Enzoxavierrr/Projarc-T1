package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoEncontradoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoPertenceAoClienteException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.StatusInvalidoParaCancelamentoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.StatusInvalidoParaPagamentoException;

@Service
public class PedidoService implements IPedidoService {
    private PedidoRepository pedidoRepository;
    private IDescontoService descontoService;
    private IImpostoService impostoService;
    private IEstoqueService estoqueService;

    public PedidoService(PedidoRepository pedidoRepository, IDescontoService descontoService,
            IImpostoService impostoService, IEstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.descontoService = descontoService;
        this.impostoService = impostoService;
        this.estoqueService = estoqueService;
    }

    @Override
    public ResultadoPedido processarPedido(Pedido pedido) {
        List<ItemPedido> itensIndisponiveis = estoqueService.verificarEstoque(pedido.getItens());
        if (!itensIndisponiveis.isEmpty()) {
            pedido.reprovar();
            return new ResultadoPedido(pedidoRepository.salvar(pedido), itensIndisponiveis);
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
    // o servico de dominio pergunta ao repo, se o pedido existir, lanca uma execao de dominio - pedidonaoEncontrado - que é tratado na camada de apresnetacao.
    

    @Override
    public Pedido pagar(long id, String cpf) {
        Pedido pedido = pedidoRepository.buscarResumoPorId(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        if (!pedido.getCliente().getCpf().equals(cpf)) {
            throw new PedidoNaoPertenceAoClienteException(id, cpf);
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new StatusInvalidoParaPagamentoException(pedido.getStatus());
        }

        pedidoRepository.atualizarStatus(id, Pedido.Status.PAGO);
        pedido.setStatus(Pedido.Status.PAGO);
        return pedido;
    }

    @Override
    public void cancelar(long id, String cpf) {
        Pedido pedido = pedidoRepository.buscarResumoPorId(id)
            .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        if (!pedido.getCliente().getCpf().equals(cpf)) {
            throw new PedidoNaoPertenceAoClienteException(id, cpf);
        }

        if (pedido.getStatus() != Pedido.Status.APROVADO) {
            throw new StatusInvalidoParaCancelamentoException(pedido.getStatus());
        }

        pedidoRepository.atualizarStatus(id, Pedido.Status.CANCELADO);
    }
}
