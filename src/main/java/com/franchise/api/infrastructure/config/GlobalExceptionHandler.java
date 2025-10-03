package com.franchise.api.infrastructure.config;

import com.franchise.api.domain.exception.BusinessRuleException;
import com.franchise.api.domain.exception.DuplicateFranchiseException;
import com.franchise.api.domain.exception.FranchiseNotFoundException;
import com.franchise.api.domain.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Manejador de Excepciones Global para Spring WebFlux.
 * Convierte excepciones de Dominio/Aplicación en respuestas HTTP adecuadas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return Mono.just(ex.getMessage());
    }


    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex) {
        String errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Validation Failed", errors)));
    }

    @ExceptionHandler(FranchiseNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(FranchiseNotFoundException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND) // HTTP 404 [1]
                .body(new ErrorResponse("Not Found", ex.getMessage())));
    }

    @ExceptionHandler(DuplicateFranchiseException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDuplicateException(DuplicateFranchiseException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT) // HTTP 409
                .body(new ErrorResponse("Conflict", ex.getMessage())));
    }

    @ExceptionHandler(BusinessRuleException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBusinessRuleException(BusinessRuleException ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // HTTP 400
                .body(new ErrorResponse("Business Rule Violation", ex.getMessage())));
    }

    private record ErrorResponse(String error, String message) {}
}
