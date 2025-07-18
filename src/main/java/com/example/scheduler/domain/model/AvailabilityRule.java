package com.example.scheduler.domain.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public record AvailabilityRule(
        UUID id,
        UUID userId,
        Integer weekday,
        LocalTime startTime,
        LocalTime endTime,
        Instant createdAt,
        Instant updatedAt) {
}