package com.projarc.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AutenticacaoGatewayFilter implements GlobalFilter, Ordered {

    private static final List<String> ROTAS_PUBLICAS = List.of(
        "/clientes/registrar",
        "/clientes/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        if (isRotaPublica(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        String cpf = validarToken(token);

        if (cpf == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Propaga o CPF autenticado como header interno para o lancheria-app
        ServerWebExchange exchangeComCpf = exchange.mutate()
            .request(r -> r.header("X-CPF-Autenticado", cpf))
            .build();

        return chain.filter(exchangeComCpf);
    }

    private boolean isRotaPublica(String path, String method) {
        if ("OPTIONS".equalsIgnoreCase(method)) return true;
        return ROTAS_PUBLICAS.stream().anyMatch(path::equals);
    }

    private String validarToken(String token) {
        // TODO: mover lógica de validação de token do AutenticacaoService do lancheria-app para cá
        return null;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
