package com.example.scheduler.adapters.dto;

public record RegisterRequest(
        String username,
        String password,
        String email
) {
}