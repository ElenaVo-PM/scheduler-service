package com.example.scheduler.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({EntityNotFoundException.class, NotFoundException.class})
    public ResponseEntity<ApiError> entityNotFoundHandler(RuntimeException exception,
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

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequestExceptions(Exception exception,
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

    @ExceptionHandler
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException exception,
                                                                  ServletWebRequest request) {
        logger.warn("Authorization failed: {}", exception.getMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> accessDeniedExceptionExceptionHandler(AccessDeniedException exception,
                                                                          ServletWebRequest request) {
        logger.warn(exception.getMessage());

        HttpStatus status = HttpStatus.FORBIDDEN;
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
        logger.warn("Unexpected exception occurred: {}", exception.getMessage(), exception);

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
