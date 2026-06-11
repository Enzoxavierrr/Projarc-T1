package com.projarc.gateway.auth;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    public String gerarToken(String cpf) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, cpf);
        return token;
    }

    public String validarToken(String token) {
        return tokens.get(token);
    }
}
