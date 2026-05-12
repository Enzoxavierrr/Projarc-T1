package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoEncontradoException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.PedidoNaoPertenceAoClienteException;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Excecoes.StatusInvalidoParaCancelamentoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ResponseEntity<String> handlePedidoNaoEncontrado(PedidoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PedidoNaoPertenceAoClienteException.class)
    public ResponseEntity<String> handlePedidoNaoPertenceAoCliente(PedidoNaoPertenceAoClienteException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(StatusInvalidoParaCancelamentoException.class)
    public ResponseEntity<String> handleStatusInvalido(StatusInvalidoParaCancelamentoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
