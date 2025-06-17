package com.example.scheduler.domain.model;

public record User(
    Long id,
    String username,
    String email,
    String passwordHash,
    boolean enabled
) {}