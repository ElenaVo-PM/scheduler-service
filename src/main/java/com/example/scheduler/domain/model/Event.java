package com.example.scheduler.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Event(
    UUID id,
    UUID ownerId,
    String title,
    String description,
    int duration,
    boolean isActive,
    int maxParticipants,
    EventType eventType,
    UUID link,
    Instant createdAt,
    Instant updatedAt
) {}