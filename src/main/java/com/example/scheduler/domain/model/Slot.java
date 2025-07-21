package com.example.scheduler.domain.model;

import java.time.Instant;

public record Slot(
        Long id,
        Long eventId,
        Instant startTime,
        Instant endTime,
        boolean isBooked
) {
}