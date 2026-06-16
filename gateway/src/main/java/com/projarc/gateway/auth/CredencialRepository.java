package com.projarc.gateway.auth;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CredencialRepository extends ReactiveCrudRepository<Credencial, String> {
    Mono<Credencial> findByEmail(String email);
}
