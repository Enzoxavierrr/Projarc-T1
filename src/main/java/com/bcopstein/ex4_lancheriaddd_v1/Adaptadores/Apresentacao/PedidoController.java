package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SolicitaStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PagarPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private SubmeterPedidoUC submeterPedidoUC;
    private SolicitaStatusPedidoUC solicitaStatusPedidoUC;
    private PagarPedidoUC pagarPedidoUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC, SolicitaStatusPedidoUC solicitaStatusPedidoUC, PagarPedidoUC pagarPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.solicitaStatusPedidoUC = solicitaStatusPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
    }

    @PostMapping("/submeter")
    @CrossOrigin("*")
    public PedidoResponse submeterPedido(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoUC.run(request);
    }

    @GetMapping("/status/{idPedido}")
    @CrossOrigin("*")
    public StatusPedidoResponse solicitaStatusUC(@PathVariable long idPedido) {
        return solicitaStatusPedidoUC.run(idPedido);
    }

    @PostMapping("/{id}/pagar")
    @CrossOrigin("*")
    public PagarPedidoResponse pagar(@PathVariable long id, @RequestBody PagarPedidoRequest request) {
        return pagarPedidoUC.executar(id, request);
    }
}