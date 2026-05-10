package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private SubmeterPedidoUC submeterPedidoUC;

    @Autowired
    public PedidoController(SubmeterPedidoUC submeterPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
    }

    @PostMapping("/submeter")
    @CrossOrigin("*")
    public PedidoResponse submeterPedido(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoUC.run(request);
    }
}