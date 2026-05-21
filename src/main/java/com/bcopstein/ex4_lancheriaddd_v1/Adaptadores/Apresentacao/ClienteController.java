package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RegistrarclienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;

@Tag(name = "Clientes", description = "UC1 e UC2 - Cadastro e autenticação de clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private RegistrarclienteUC registrarclienteUC;


    public ClienteController(RegistrarclienteUC registrarclienteUC) {
        this.registrarclienteUC = registrarclienteUC;
    }

    @Operation(
            summary = "Registrar cliente (UC1)",
            description = "Cadastra um novo cliente no sistema. CPF e e-mail devem ser únicos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                        {
                          "cpf": "12345678900",
                          "nome": "João Silva",
                          "email": "joao@email.com",
                          "senha": "minhasenha123",
                          "celular": "51999999999",
                          "endereco": "Rua das Flores, 100"
                        }
                        """)
                )
            ),
            responses = {
                @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso"),
                @ApiResponse(responseCode = "409", description = "CPF ou e-mail já cadastrado")
            }
        )


        @PostMapping("/registrar")
        @ResponseStatus(HttpStatus.CREATED)
        @CrossOrigin("*")
    public void registrarCliente(@RequestBody RegistrarClienteRequest request) {
        registrarclienteUC.executar(request);
    }

}