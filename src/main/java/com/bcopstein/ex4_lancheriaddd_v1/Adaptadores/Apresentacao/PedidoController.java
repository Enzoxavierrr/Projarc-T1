package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosClienteEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.ListarPedidosEntreguesUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SolicitaStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PagarPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoListagemResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.CancelarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private SubmeterPedidoUC submeterPedidoUC;
    private SolicitaStatusPedidoUC solicitaStatusPedidoUC;
    private PagarPedidoUC pagarPedidoUC;
    private CancelarPedidoUC cancelarPedidoUC;
    private ListarPedidosEntreguesUC listarPedidosEntreguesUC;
    private ListarPedidosClienteEntreguesUC listarPedidosClienteEntreguesUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC,
                            SolicitaStatusPedidoUC solicitaStatusPedidoUC,
                            PagarPedidoUC pagarPedidoUC,
                            CancelarPedidoUC cancelarPedidoUC,
                            ListarPedidosEntreguesUC listarPedidosEntreguesUC,
                            ListarPedidosClienteEntreguesUC listarPedidosClienteEntreguesUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.solicitaStatusPedidoUC = solicitaStatusPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
        this.listarPedidosEntreguesUC = listarPedidosEntreguesUC;
        this.listarPedidosClienteEntreguesUC = listarPedidosClienteEntreguesUC;
    }

    @PostMapping("/submeter")
    @CrossOrigin("*")
    public PedidoResponse submeterPedido(@RequestBody SubmeterPedidoRequest request,
                                         HttpServletRequest httpRequest) {
        String cpfAutenticado = (String) httpRequest.getAttribute("cpfAutenticado");
        request.setClienteCpf(cpfAutenticado);
        return submeterPedidoUC.run(request);
    }

    @GetMapping("/status/{idPedido}")
    @CrossOrigin("*")
    public StatusPedidoResponse solicitaStatusUC(@PathVariable long idPedido,
                                                 HttpServletRequest httpRequest) {
        String cpfAutenticado = (String) httpRequest.getAttribute("cpfAutenticado");
        return solicitaStatusPedidoUC.run(idPedido, cpfAutenticado);
    }

    @PostMapping("/{id}/pagar")
    @CrossOrigin("*")
    public PagarPedidoResponse pagar(@PathVariable long id,
                                     HttpServletRequest httpRequest) {
        String cpfAutenticado = (String) httpRequest.getAttribute("cpfAutenticado");
        PagarPedidoRequest request = new PagarPedidoRequest(cpfAutenticado);
        return pagarPedidoUC.executar(id, request);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin("*")
    public CancelarPedidoResponse cancelarPedido(@PathVariable long id,
                                                 HttpServletRequest httpRequest) {
        String cpfAutenticado = (String) httpRequest.getAttribute("cpfAutenticado");
        cancelarPedidoUC.executar(id, cpfAutenticado);
        return new CancelarPedidoResponse("Pedido cancelado com sucesso.");
    }

    @GetMapping("/entregues")
    @CrossOrigin("*")
    public List<PedidoListagemResponse> listarEntregues(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return listarPedidosEntreguesUC.run(inicio, fim);
    }

    @GetMapping("/entregues/meus")
    @CrossOrigin("*")
    public List<PedidoListagemResponse> listarEntreguesDoCliente(
            HttpServletRequest httpRequest,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        String cpfAutenticado = (String) httpRequest.getAttribute("cpfAutenticado");
        return listarPedidosClienteEntreguesUC.run(cpfAutenticado, inicio, fim);
    }
}
