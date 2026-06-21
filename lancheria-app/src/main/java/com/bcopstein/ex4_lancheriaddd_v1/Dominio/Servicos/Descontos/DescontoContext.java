package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Descontos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

@Component
public class DescontoContext {
    private final List<ICalculoDesconto> estrategias;
    private ICalculoDesconto estrategiaAtiva;

    @Autowired
    public DescontoContext(List<ICalculoDesconto> estrategias) {
        this.estrategias = estrategias;
        this.estrategiaAtiva = buscarPorCodigo("DescontoFidelidade")
            .orElseThrow(() -> new IllegalStateException(
                "Estrategia padrao 'DescontoFidelidade' nao encontrada"));
    }

    public List<String> listarCodigosDisponiveis() {
        return estrategias.stream()
            .map(ICalculoDesconto::getCodigo)
            .toList();
    }

    public void definirEstrategiaAtiva(String codigo) {
        this.estrategiaAtiva = buscarPorCodigo(codigo)
            .orElseThrow(() -> new IllegalArgumentException(
                "Estrategia de desconto nao encontrada: " + codigo));
    }

    public double calcular(double valorItem, Cliente cliente) {
        return estrategiaAtiva.calcular(valorItem, cliente);
    }

    public String getCodigoAtivo() {
        return estrategiaAtiva.getCodigo();
    }

    private java.util.Optional<ICalculoDesconto> buscarPorCodigo(String codigo) {
        return estrategias.stream()
            .filter(e -> e.getCodigo().equals(codigo))
            .findFirst();
    }
}
