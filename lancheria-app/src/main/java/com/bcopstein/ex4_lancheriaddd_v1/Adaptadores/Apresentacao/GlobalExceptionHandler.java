package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.ClienteJaCadastradoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.CpfInvalidoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.CredenciaisInvalidasException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PagamentoNaoEfetuadoException;
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
    }

    @ExceptionHandler(PedidoNaoPertenceAoClienteException.class)
    public ResponseEntity<String> handlePedidoNaoPertenceAoCliente(PedidoNaoPertenceAoClienteException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Pedido nao pertence ao cliente autenticado.");
    }

    @ExceptionHandler(StatusInvalidoParaCancelamentoException.class)
    public ResponseEntity<String> handleStatusInvalido(StatusInvalidoParaCancelamentoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este pedido nao pode ser cancelado no status atual.");
    }

    @ExceptionHandler(StatusInvalidoParaPagamentoException.class)
    public ResponseEntity<String> handleStatusInvalidoPagamento(StatusInvalidoParaPagamentoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Este pedido nao pode ser pago no status atual.");
    }

    @ExceptionHandler(PagamentoNaoEfetuadoException.class)
    public ResponseEntity<String> handlePagamentoNaoEfetuado(PagamentoNaoEfetuadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ClienteJaCadastradoException.class)
    public ResponseEntity<String> handleClienteJaCadastrado(ClienteJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CpfInvalidoException.class)
    public ResponseEntity<String> handleCpfInvalido(CpfInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<String> handleCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
