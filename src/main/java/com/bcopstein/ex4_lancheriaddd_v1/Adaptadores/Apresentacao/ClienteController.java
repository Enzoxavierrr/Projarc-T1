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

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AutenticarUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RegistrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticarResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarClienteResponse;

@Tag(name = "Clientes", description = "UC1 e UC2 - Cadastro e autenticação de clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private RegistrarClienteUC registrarClienteUC;
    private AutenticarUC autenticarUC;

    public ClienteController(RegistrarClienteUC registrarClienteUC, AutenticarUC autenticarUC) {
        this.registrarClienteUC = registrarClienteUC;
        this.autenticarUC = autenticarUC;
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
            @ApiResponse(responseCode = "400", description = "CPF inválido (deve ter 11 dígitos)"),
            @ApiResponse(responseCode = "409", description = "CPF ou e-mail já cadastrado")
        }
    )
    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("*")
    public RegistrarClienteResponse registrarCliente(@RequestBody RegistrarClienteRequest request) {
        return registrarClienteUC.executar(request);
    }

    @Operation(
        summary = "Autenticar cliente (UC2)",
        description = "Autentica o cliente com email e senha. Retorna o CPF do cliente em caso de sucesso.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(value = """
                    {
                      "email": "huguinho.pato@email.com",
                      "senha": "123456"
                    }
                    """)
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso, retorna o CPF"),
            @ApiResponse(responseCode = "401", description = "Email ou senha inválidos")
        }
    )
    @PostMapping("/login")
    @CrossOrigin("*")
    public AutenticarResponse login(@RequestBody AutenticarRequest request) {
        return autenticarUC.executar(request);
    }
}
