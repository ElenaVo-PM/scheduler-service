package com.example.scheduler.adapters.dto;

import java.time.Instant;

public record CreateEventRequest(
    String title,
    String description,
    int maxParticipants,
    String eventType,
    Instant startTime,
    Instant endTime
) {}