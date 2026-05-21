package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.ClienteJaCadastradoException;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.buscarPorCpf(cpf);
    }

    public void registrar(RegistrarClienteRequest request) {
        if (clienteRepository.buscarPorCpf(request.cpf()) != null) {
            throw new ClienteJaCadastradoException("CPF", request.cpf());
        }
        if (clienteRepository.buscarPorEmail(request.email()) != null) {
            throw new ClienteJaCadastradoException("E-mail", request.email());
        }

        Cliente novoCliente = new Cliente(
            request.cpf(),
            request.nome(),
            request.celular(),
            request.endereco(),
            request.email(),
            request.senha()
        );
        clienteRepository.salvar(novoCliente);
    }
}
