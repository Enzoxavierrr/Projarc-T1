package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.AutenticacaoService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AutenticacaoFilter extends OncePerRequestFilter {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoFilter(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String method = request.getMethod();

        if (contextPath != null && !contextPath.isBlank() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }
        if (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }

        if ("OPTIONS".equalsIgnoreCase(method)) return true;
        if (path.startsWith("/swagger-ui")) return true;
        if (path.startsWith("/v3/api-docs")) return true;
        if (path.startsWith("/h2-console")) return true;
        if (path.equals("/") || path.isBlank()) return true;
        if (path.equals("/clientes/registrar")) return true;
        if (path.equals("/clientes/login")) return true;

        // UCs com autenticacao obrigatoria (A): UC3, UC4, UC5, UC6, UC7
        boolean uc3 = path.startsWith("/cardapio");
        boolean uc4 = "POST".equalsIgnoreCase(method) && path.equals("/pedidos/submeter");
        boolean uc5 = "GET".equalsIgnoreCase(method) && path.startsWith("/pedidos/status/");
        boolean uc6 = "DELETE".equalsIgnoreCase(method) && path.matches("^/pedidos/\\d+$");
        boolean uc7 = "POST".equalsIgnoreCase(method) && path.matches("^/pedidos/\\d+/pagar$");
        boolean endpointProtegido = uc3 || uc4 || uc5 || uc6 || uc7;
        return !endpointProtegido;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token de autenticacao ausente ou invalido.\"}");
            return;
        }

        String token = authHeader.substring(7);
        String cpf = autenticacaoService.validarToken(token);

        if (cpf == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token de autenticacao ausente ou invalido.\"}");
            return;
        }

        request.setAttribute("cpfAutenticado", cpf);
        filterChain.doFilter(request, response);
    }
}
