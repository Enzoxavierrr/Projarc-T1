package com.projarc.gateway.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
public class AuthController {

    private final WebClient.Builder webClientBuilder;
    private final TokenService tokenService;

    public AuthController(WebClient.Builder webClientBuilder, TokenService tokenService) {
        this.webClientBuilder = webClientBuilder;
        this.tokenService = tokenService;
    }

    /**
     * Intercepta o login antes que chegue ao lancheria-app.
     * 1) Chama /clientes/validar-credenciais no lancheria-app para checar email+senha
     * 2) Recebe o CPF de volta
     * 3) Gera o token aqui no gateway e devolve ao cliente
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> credenciais) {
        return webClientBuilder.build()
            .post()
            .uri("lb://lancheria-app/clientes/validar-credenciais")
            .bodyValue(credenciais)
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> {
                String cpf = (String) response.get("cpf");
                String token = tokenService.gerarToken(cpf);
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "mensagem", "Login realizado com sucesso."
                ));
            })
            .onErrorResume(WebClientResponseException.class, e ->
                Mono.just(ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Credenciais invalidas.")))
            );
    }
}
