package com.projarc.gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.projarc.gateway.auth.TokenService;
import com.projarc.gateway.auth.TokenService.TokenData;

import reactor.core.publisher.Mono;

@Component
public class AutenticacaoGatewayFilter implements GlobalFilter, Ordered {

    private final TokenService tokenService;

    public AutenticacaoGatewayFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    private static final List<String> ROTAS_PUBLICAS = List.of(
        "/",
        "/clientes/registrar",
        "/clientes/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path   = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        if (isRotaPublica(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token       = authHeader.substring(7);
        TokenData tokenData = tokenService.validarToken(token);

        if (tokenData == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Bloqueia acesso externo ao estoque para não-administradores
        if (path.startsWith("/estoque/") && !"ADMIN".equals(tokenData.role())) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Injeta CPF e role como headers internos para o lancheria-app
        ServerWebExchange exchangeComCpf = exchange.mutate()
            .request(r -> r
                .header("X-CPF-Autenticado",  tokenData.cpf())
                .header("X-Role-Autenticado", tokenData.role()))
            .build();

        return chain.filter(exchangeComCpf);
    }

    private boolean isRotaPublica(String path, String method) {
        if ("OPTIONS".equalsIgnoreCase(method)) return true;
        return ROTAS_PUBLICAS.stream().anyMatch(path::equals);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
