package com.example.scheduler.adapters.dto;

import java.time.Instant;

public record BookingResponse(
    Long id,
    Long eventId,
    Long slotId,
    Instant startTime,
    Instant endTime,
    boolean isCancelled
) {}