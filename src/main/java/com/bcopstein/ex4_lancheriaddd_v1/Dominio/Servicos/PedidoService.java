package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ResultadoPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoEncontradoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoPertenceAoClienteException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.StatusInvalidoParaCancelamentoException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.PedidoRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;

@Service
public class PedidoService implements IPedidoService {
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

        public ResultadoPedido processarPedido(Pedido pedido){
            //verificar estoque
            List<ItemPedido> itensIndisponiveis = estoqueService.verificarEstoque(pedido.getItens());
            if(!itensIndisponiveis.isEmpty()){
                pedido.setStatus(Pedido.Status.REPROVADO);
                return new ResultadoPedido(pedido, itensIndisponiveis);
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

            //salvar pedido
            return new ResultadoPedido(pedidoRepository.salvar(pedido), List.of());
        }

        @Override
        public Optional<Pedido> buscarPorId(long id) {
            return pedidoRepository.buscarPorId(id);
        }

        @Override
        public void cancelar(long id, String cpf) {
            Pedido pedido = pedidoRepository.buscarPorId(id)
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
