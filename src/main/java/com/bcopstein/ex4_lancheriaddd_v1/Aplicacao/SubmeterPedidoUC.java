package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemPedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Pedido;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.PedidoService;

@Component
public class SubmeterPedidoUC {
    private PedidoService pedidoService;
    private ClienteRepository clienteRepository;
    private ProdutosRepository produtosRepository;

    @Autowired
    public SubmeterPedidoUC(PedidoService pedidoService,
                            ClienteRepository clienteRepository,
                            ProdutosRepository produtosRepository) {
        this.pedidoService = pedidoService;
        this.clienteRepository = clienteRepository;
        this.produtosRepository = produtosRepository;
    }

    public PedidoResponse run(SubmeterPedidoRequest request) {

        Cliente cliente = clienteRepository.buscarPorCpf(request.getClienteCpf());

        // monta a lista de itens
        List<ItemPedido> itens = request.getItens().stream()
            .map(itemReq -> {
                Produto produto = produtosRepository.recuperaProdutoPorid(itemReq.getProdutoId());
                return new ItemPedido(produto, itemReq.getQuantidade());
            })
            .toList();

        // monta o pedido
        Pedido pedido = new Pedido(0, cliente, null, itens, Pedido.Status.NOVO, 0, 0, 0, 0);

        // processa
        Pedido pedidoProcessado = pedidoService.processarPedido(pedido);

        return new PedidoResponse(pedidoProcessado, List.of());
    }
}