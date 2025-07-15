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

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final Clock clock;

    public GlobalExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            NotFoundException.class
    })
    public ResponseEntity<ApiError> entityNotFoundHandler(
            RuntimeException exception,
            ServletWebRequest request
    ) {
        logger.warn("Entity not found: {}", exception.getMessage());
        return toResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequest().getRequestURI()
        );
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequestExceptions(
            Exception exception,
            ServletWebRequest request
    ) {
        logger.warn("Message is not readable: {}", exception.getMessage());
        return toResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequest().getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            ServletWebRequest request
    ) {
        logger.warn("Validation failed: {}", exception.getMessage());
        return toResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequest().getRequestURI()
        );
    }

    @ExceptionHandler({
            BadCredentialsException.class,
            UserNotAuthorizedException.class
    })
    public ResponseEntity<ApiError> handleUnauthorizedExceptions(
            RuntimeException exception,
            ServletWebRequest request
    ) {
        logger.warn("Authorization failed: {}", exception.getMessage());
        return toResponse(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                request.getRequest().getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> accessDeniedExceptionExceptionHandler(
            AccessDeniedException exception,
            ServletWebRequest request
    ) {
        logger.warn(exception.getMessage());

        return toResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                request.getRequest().getRequestURI()
        );
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotEnoughAuthorityException(
            NotEnoughAuthorityException exception,
            ServletWebRequest request
    ) {
        logger.warn("Operation blocked: {}", exception.getMessage());
        return toResponse(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                request.getRequest().getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> unexpectedExceptionHandler(
            Exception exception,
            ServletWebRequest request
    ) {
        logger.warn("Unexpected exception occurred: {}", exception.getMessage(), exception);
        return toResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                request.getRequest().getRequestURI()
        );
    }

    private ResponseEntity<ApiError> toResponse(HttpStatus status, String message, String path) {
        ApiError error = new ApiError(
                Instant.now(clock).truncatedTo(ChronoUnit.SECONDS),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }
}
