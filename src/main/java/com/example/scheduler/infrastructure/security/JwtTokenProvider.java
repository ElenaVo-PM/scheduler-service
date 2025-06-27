package com.example.scheduler.infrastructure.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secretKey = "very_secret_key"; // Лучше вынести в application.yml
    private final long validityInMillis = 3600000; // 1 час

    public String generateToken(String username) {
        return null;
    }

    public String getUsername(String token) {
        return null;
    }
}