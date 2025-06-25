package com.example.scheduler.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Profile(
        UUID userId,
        String description,
        boolean isActive,
        String logo,
        Instant createdAt,
        Instant updatedAt
) {}