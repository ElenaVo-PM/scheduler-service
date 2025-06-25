package com.example.scheduler.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String token) {
    super(String.format("Token [%s] is invalid", token));
  }
}
