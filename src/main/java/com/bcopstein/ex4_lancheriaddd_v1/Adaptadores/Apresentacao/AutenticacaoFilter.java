package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.io.IOException;

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
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Endpoints públicos: registro, login, welcome, swagger/openapi
        if (path.equals("/") || path.equals("")) return true;
        if (path.equals("/clientes/registrar")) return true;
        if (path.equals("/clientes/login")) return true;
        if (path.startsWith("/swagger-ui")) return true;
        if (path.startsWith("/v3/api-docs")) return true;
        if (path.startsWith("/h2-console")) return true;
        if ("OPTIONS".equalsIgnoreCase(method)) return true;

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token de autenticação ausente ou inválido.\"}");
            return;
        }

        String token = authHeader.substring(7);
        String cpf = autenticacaoService.validarToken(token);

        if (cpf == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token de autenticação ausente ou inválido.\"}");
            return;
        }

        request.setAttribute("cpfAutenticado", cpf);
        filterChain.doFilter(request, response);
    }
}
