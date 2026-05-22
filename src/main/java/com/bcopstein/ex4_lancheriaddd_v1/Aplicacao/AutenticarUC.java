package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticarResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

@Component
public class AutenticarUC {
    private ClienteService clienteService;

    public AutenticarUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public AutenticarResponse executar(AutenticarRequest request) {
        return clienteService.autenticar(request);
    }
}
