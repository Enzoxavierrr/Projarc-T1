package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.CancelarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.PagarPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SolicitaStatusPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.SubmeterPedidoUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.PagarPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.SubmeterPedidoRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PagarPedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.PedidoResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.StatusPedidoResponse;

@Tag(name = "Pedidos", description = "UC4, UC5, UC6, UC7 - Submissão, consulta, cancelamento e pagamento de pedidos")
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private SubmeterPedidoUC submeterPedidoUC;
    private SolicitaStatusPedidoUC solicitaStatusPedidoUC;
    private PagarPedidoUC pagarPedidoUC;
    private CancelarPedidoUC cancelarPedidoUC;

    public PedidoController(SubmeterPedidoUC submeterPedidoUC,
                            SolicitaStatusPedidoUC solicitaStatusPedidoUC,
                            PagarPedidoUC pagarPedidoUC,
                            CancelarPedidoUC cancelarPedidoUC) {
        this.submeterPedidoUC = submeterPedidoUC;
        this.solicitaStatusPedidoUC = solicitaStatusPedidoUC;
        this.pagarPedidoUC = pagarPedidoUC;
        this.cancelarPedidoUC = cancelarPedidoUC;
    }

    @Operation(
        summary = "Submeter pedido (UC4)",
        description = "Cria um novo pedido. O sistema verifica estoque, aplica desconto e imposto. Retorna status APROVADO ou REPROVADO.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "clienteCpf": "9001",
                      "enderecoEntrega": "Rua das Flores, 100",
                      "itens": [
                        { "produtoId": 3, "quantidade": 1 },
                        { "produtoId": 5, "quantidade": 2 }
                      ]
                    }
                    """)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Pedido criado (aprovado ou reprovado)"),
            @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado")
        }
    )
    @PostMapping("/submeter")
    @CrossOrigin("*")
    public PedidoResponse submeterPedido(@RequestBody SubmeterPedidoRequest request) {
        return submeterPedidoUC.run(request);
    }

    @Operation(
        summary = "Consultar status do pedido (UC5)",
        description = "Retorna o status atual de um pedido pelo seu ID. Possíveis status: NOVO, APROVADO, REPROVADO, PAGO, AGUARDANDO, PREPARACAO, PRONTO, TRANSPORTE, ENTREGUE, CANCELADO.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
        }
    )
    @GetMapping("/status/{idPedido}")
    @CrossOrigin("*")
    public StatusPedidoResponse solicitaStatusUC(@Parameter(description = "ID do pedido") @PathVariable long idPedido) {
        return solicitaStatusPedidoUC.run(idPedido);
    }

    @Operation(
        summary = "Pagar pedido (UC7)",
        description = "Processa o pagamento de um pedido com status APROVADO. Após o pagamento, o pedido é encaminhado para a cozinha.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "cpf": "9001"
                    }
                    """)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Pagamento realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Pedido não está com status APROVADO"),
            @ApiResponse(responseCode = "403", description = "CPF não pertence ao dono do pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
        }
    )
    @PostMapping("/{id}/pagar")
    @CrossOrigin("*")
    public PagarPedidoResponse pagar(@Parameter(description = "ID do pedido") @PathVariable long id,
                                     @RequestBody PagarPedidoRequest request) {
        return pagarPedidoUC.executar(id, request);
    }

    @Operation(
        summary = "Cancelar pedido (UC6)",
        description = "Cancela um pedido com status APROVADO. O CPF deve pertencer ao cliente dono do pedido.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Pedido não pode ser cancelado no status atual"),
            @ApiResponse(responseCode = "403", description = "CPF não pertence ao dono do pedido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
        }
    )
    @DeleteMapping("/{id}")
    @CrossOrigin("*")
    public ResponseEntity<Void> cancelarPedido(
            @Parameter(description = "ID do pedido") @PathVariable long id,
            @Parameter(description = "CPF do cliente dono do pedido") @RequestParam String cpf) {
        cancelarPedidoUC.executar(id, cpf);
        return ResponseEntity.noContent().build();
    }
}
