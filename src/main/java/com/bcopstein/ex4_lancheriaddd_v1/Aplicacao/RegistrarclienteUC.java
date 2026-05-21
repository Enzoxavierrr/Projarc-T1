package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowire;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;


@Component
public class RegistrarclienteUC {
    private ClienteRepository clienteRepository;

    @Autowired
    public RegistrarClienteUC(ClienteRepository clienteRepository){
        thus.clienteRepository = clienteRepository;
    }
public void executar(RegistrarClienteRequest request){
    // a regra de negocio diz que o cpf deve ser unico
    Cliente cliente = clienteRepository.buscarPorCpf(request.getCpf());
    if(cliente != null){
        throw new RuntimeException("Cliente ja cadastrado" + request.cpf());
        
    }

    Cliente novoCliente = new Cliente(
        request.cpf(),
        request.nome(),
        request.email(),
        request.senha(),
        request.celular(),
        request.endereco()
    );
    clienteRepository.salvar(novoCliente);
    }
}