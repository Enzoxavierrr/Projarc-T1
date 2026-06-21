package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.Impostos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImpostoFactory {

    @Value("${imposto.ativo:lei_1234}")
    private String impostoAtivo;

    @Bean
    public ICalculoImposto calculoImposto() {
        switch (impostoAtivo) {
            case "lei_1234":
                return new ImpostoLei1234();
            case "lei_5678":
                return new ImpostoLei5678();
            default:
                throw new IllegalArgumentException(
                    "Lei de imposto desconhecida: '" + impostoAtivo
                    + "'. Valores válidos: lei_1234, lei_5678.");
        }
    }
}
