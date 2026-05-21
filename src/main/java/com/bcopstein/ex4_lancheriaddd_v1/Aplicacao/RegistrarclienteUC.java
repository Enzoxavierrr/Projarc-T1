package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ClienteService;

@Component
public class RegistrarclienteUC {
    private ClienteService clienteService;

    public RegistrarclienteUC(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void executar(RegistrarClienteRequest request) {
        clienteService.registrar(request);
    }
}
