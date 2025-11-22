package com.hortifruti.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Manipulador de exceções global para a aplicação.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Construtor padrão.
     */
    public GlobalExceptionHandler() {}

    /**
     * Manipula exceções do tipo IllegalArgumentException.
     * @param ex A exceção
     * @param request A requisição web
     * @return Uma resposta com status 400 (Bad Request) e a mensagem da exceção
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manipula exceções genéricas do tipo Exception.
     * @param ex A exceção
     * @param request A requisição web
     * @return Uma resposta com status 500 (Internal Server Error) e uma mensagem genérica
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocorreu um erro inesperado: " + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Manipula falta de parâmetros obrigatórios na requisição (ex.: formaPagamento).
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "Parâmetro obrigatório ausente: " + ex.getParameterName());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}