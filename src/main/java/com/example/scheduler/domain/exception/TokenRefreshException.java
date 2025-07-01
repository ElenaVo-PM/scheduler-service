package com.example.scheduler.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TokenRefreshException extends RuntimeException {

    public TokenRefreshException(String token) {
        super(String.format("Failed refresh token [%s]", token));
    }
}
