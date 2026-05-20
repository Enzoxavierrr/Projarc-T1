package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoEncontradoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoPertenceAoClienteException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.StatusInvalidoParaCancelamentoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.StatusInvalidoParaPagamentoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handlePedidoNaoEncontrado(PedidoNaoEncontradoException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body); 
    } // aqui as exececoes de dominio sao capturadas centralizadamente aqui no GlobalExceptionHandler. 

    @ExceptionHandler(PedidoNaoPertenceAoClienteException.class)
    public ResponseEntity<String> handlePedidoNaoPertenceAoCliente(PedidoNaoPertenceAoClienteException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Pedido não pertence ao cliente autenticado.");
    }

    @ExceptionHandler(StatusInvalidoParaCancelamentoException.class)
    public ResponseEntity<String> handleStatusInvalido(StatusInvalidoParaCancelamentoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este pedido não pode ser cancelado no status atual.");
    }

    @ExceptionHandler(StatusInvalidoParaPagamentoException.class)
    public ResponseEntity<String> handleStatusInvalidoPagamento(StatusInvalidoParaPagamentoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este pedido não pode ser pago no status atual.");
    }
}
