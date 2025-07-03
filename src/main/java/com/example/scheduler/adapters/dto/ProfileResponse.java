package com.example.scheduler.adapters.dto;

import java.time.Instant;
import java.util.UUID;

public record ProfileResponse(
        UUID userId,
        String username,
        String description,
        boolean isActive,
        String logo
) {}