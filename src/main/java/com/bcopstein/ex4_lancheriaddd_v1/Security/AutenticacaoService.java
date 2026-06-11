package com.bcopstein.ex4_lancheriaddd_v1.Security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {
    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    public String gerarToken(String cpf) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, cpf);
        return token;
    }

    public String validarToken(String token) {
        return tokens.get(token);
    }

    public void revogarToken(String token) {
        tokens.remove(token);
    }
}
