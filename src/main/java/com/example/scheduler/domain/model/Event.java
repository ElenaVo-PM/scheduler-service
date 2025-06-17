package com.example.scheduler.domain.model;

import java.time.Instant;

public record Event(
    Long id,
    Long ownerId,
    String title,
    String description,
    boolean isActive,
    int maxParticipants,
    String eventType,
    String link,
    Instant createdAt,
    Instant updatedAt
) {}