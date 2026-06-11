package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ProdutoService;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ResultadoPedido;

@Component  
public class SubmeterPedidoUC {
    private PedidoService pedidoService;
    private ClienteService clienteService;
    private ProdutoService produtoService;

    public SubmeterPedidoUC(PedidoService pedidoService,
                            ClienteService clienteService,
                            ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public PedidoResponse run(SubmeterPedidoRequest request) {

        Cliente cliente = clienteService.buscarPorCpf(request.getClienteCpf());

        List<ItemPedido> itens = request.getItens().stream()
            .map(itemReq -> {
                Produto produto = produtoService.recuperaProdutoPorId(itemReq.getProdutoId());
                return new ItemPedido(produto, itemReq.getQuantidade());
            })
            .toList();

        Pedido pedido = new Pedido(0, cliente, null, itens, Pedido.Status.NOVO, 0, 0, 0, 0, request.getEnderecoEntrega());

        // processa
        ResultadoPedido resultado = pedidoService.processarPedido(pedido);

        return new PedidoResponse(resultado.pedido(), resultado.itensIndisponiveis());
    }
}