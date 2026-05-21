package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

@Component
public class RegistrarClienteUC {
    private ClienteService clienteService;

    public RegistrarClienteUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public RegistrarClienteResponse executar(RegistrarClienteRequest request) {
        return clienteService.registrar(request);
    }
}
