package com.example.scheduler.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> entityNotFoundHandler(EntityNotFoundException exception,
                                                          ServletWebRequest request) {
        logger.warn("Entity not found: {}", exception.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        String path = request.getRequest().getRequestURI();

        ApiError apiError = new ApiError(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                path);

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> httpMessageNotReadableHandler(HttpMessageNotReadableException exception,
                                                                  ServletWebRequest request) {
        logger.warn("Message is not readable: {}", exception.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = request.getRequest().getRequestURI();

        ApiError apiError = new ApiError(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                path);

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                    ServletWebRequest request) {
        logger.warn("Validation failed: {}", exception.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String path = request.getRequest().getRequestURI();

        ApiError apiError = new ApiError(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                path);

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unexpectedExceptionHandler(Exception exception,
                                                                ServletWebRequest request) {
        logger.warn("Unexpected exception occurred: {}", exception.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String path = request.getRequest().getRequestURI();

        ApiError apiError = new ApiError(Instant.now().truncatedTo(ChronoUnit.SECONDS),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                path);

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(apiError);
    }
}
