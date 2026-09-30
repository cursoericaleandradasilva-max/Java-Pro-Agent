package com.erica.missbrasil.infrastructure.adapter.in.web;

import com.erica.missbrasil.domain.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ProblemDetail handleRegraNegocio(RegraNegocioException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Regra de Negócio Violada");
        problem.setType(URI.create("https://missbrasilcontinental.com/erros/regra-negocio"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidacaoCampos(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados inválidos enviados no formulário de inscrição");
        problem.setTitle("Erro de Validação");
        problem.setType(URI.create("https://missbrasilcontinental.com/erros/validacao"));

        Map<String, String> erros = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        problem.setProperty("erros", erros);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeral(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor");
        problem.setTitle("Erro Interno");
        problem.setType(URI.create("https://missbrasilcontinental.com/erros/interno"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
