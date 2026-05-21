package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos;

import org.springframework.stereotype.Service;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.AutenticarRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Requests.RegistrarClienteRequest;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.AutenticarResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses.RegistrarClienteResponse;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ClienteRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.ClienteJaCadastradoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.CpfInvalidoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.CredenciaisInvalidasException;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.buscarPorCpf(cpf);
    }

    public RegistrarClienteResponse registrar(RegistrarClienteRequest request) {
        // valida formato do CPF: apenas dígitos e exatamente 11 caracteres
        if (!request.cpf().matches("\\d{11}")) {
            throw new CpfInvalidoException(request.cpf());
        }

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

        return new RegistrarClienteResponse(
            "Usuario @" + request.nome() + " cadastrado com sucesso."
        );
    }

    public AutenticarResponse autenticar(AutenticarRequest request) {
        
        Cliente cliente = clienteRepository.buscarPorEmail(request.email());

        // se não encontrou ou a senha não bate → mesma exceção (segurança)
        if (cliente == null || !cliente.getSenha().equals(request.senha())) {
            throw new CredenciaisInvalidasException();
        }

        return new AutenticarResponse(cliente.getCpf());
    }
}
