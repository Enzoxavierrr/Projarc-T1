package com.projarc.gateway.auth;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public record TokenData(String cpf, String role) {}

    private final Map<String, TokenData> tokens = new ConcurrentHashMap<>();

    public String gerarToken(String cpf, String role) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenData(cpf, role));
        return token;
    }

    public TokenData validarToken(String token) {
        return tokens.get(token);
    }
}
