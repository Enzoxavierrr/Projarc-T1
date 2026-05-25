package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.AutenticarUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RegistrarClienteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticarResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarClienteResponse;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private RegistrarClienteUC registrarClienteUC;
    private AutenticarUC autenticarUC;

    public ClienteController(RegistrarClienteUC registrarClienteUC, AutenticarUC autenticarUC) {
        this.registrarClienteUC = registrarClienteUC;
        this.autenticarUC = autenticarUC;
    }

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("*")
    public RegistrarClienteResponse registrarCliente(@RequestBody RegistrarClienteRequest request) {
        return registrarClienteUC.executar(request);
    }

    @PostMapping("/login")
    @CrossOrigin("*")
    public AutenticarResponse login(@RequestBody AutenticarRequest request) {
        return autenticarUC.executar(request);
    }
}
