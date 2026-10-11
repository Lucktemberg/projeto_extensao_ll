package com.doacoes.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> tratarIllegalArgument(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(mensagem(exception.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> tratarAcessoNegado() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(mensagem("Acesso negado"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> tratarNaoEncontrado(
            EntityNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(mensagem(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> tratarValidacao(
            MethodArgumentNotValidException exception) {

        Map<String, String> erros = mensagem(
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getDefaultMessage())
                        .filter(texto -> texto != null && !texto.isBlank())
                        .findFirst()
                        .orElse("Verifique os dados informados")
        );

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        erros.put(error.getField(), error.getDefaultMessage())
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> tratarTipoInvalido(
            MethodArgumentTypeMismatchException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(mensagem("Identificador inválido."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> tratarIntegridade(
            DataIntegrityViolationException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(mensagem(
                        "Não foi possível salvar o registro. Verifique se já existe um cadastro igual."
                ));
    }

    private Map<String, String> mensagem(String texto) {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", texto);
        erro.put("mensagem", texto);
        return erro;
    }
}