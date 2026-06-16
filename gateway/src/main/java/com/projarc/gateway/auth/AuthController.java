package com.projarc.gateway.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
public class AuthController {

    private final CredencialRepository credencialRepository;
    private final TokenService tokenService;
    private final WebClient.Builder webClientBuilder;

    public AuthController(CredencialRepository credencialRepository,
                          TokenService tokenService,
                          WebClient.Builder webClientBuilder) {
        this.credencialRepository = credencialRepository;
        this.tokenService         = tokenService;
        this.webClientBuilder     = webClientBuilder;
    }

    /**
     * Login: valida email+senha no banco do próprio gateway e gera o token aqui.
     * Não chama o lancheria-app.
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String senha = body.get("senha");

        return credencialRepository.findByEmail(email)
            .filter(c -> c.getSenha().equals(senha))
            .map(c -> {
                String token = tokenService.gerarToken(c.getCpf());
                return ResponseEntity.ok(Map.of(
                    "token", token,
                    "mensagem", "Login realizado com sucesso."
                ));
            })
            .switchIfEmpty(Mono.just(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", "Credenciais invalidas."))
            ));
    }

    /**
     * Registro: salva credenciais no banco do gateway e repassa o cadastro completo
     * ao lancheria-app (nome, celular, endereço etc.).
     */
    @PostMapping("/registrar")
    public Mono<ResponseEntity<Map<String, String>>> registrar(@RequestBody Map<String, Object> body) {
        String cpf   = (String) body.get("cpf");
        String email = (String) body.get("email");
        String senha = (String) body.get("senha");

        return credencialRepository.save(new Credencial(cpf, email, senha))
            .flatMap(saved ->
                webClientBuilder.build()
                    .post()
                    .uri("lb://lancheria-app/clientes/registrar")
                    .bodyValue(body)
                    .retrieve()
                    .toEntity(Map.class)
                    .map(resp -> ResponseEntity
                        .status(resp.getStatusCode())
                        .body((Map<String, String>) resp.getBody()))
            )
            .onErrorResume(e -> Mono.just(
                ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", e.getMessage()))
            ));
    }
}
