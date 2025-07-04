package com.example.scheduler.adapters.dto;

import com.example.scheduler.domain.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateEventRequest(
        @NotNull
        @Size(min = 1, max = 255)
        String title,
        @NotNull
        @Size(min = 1, max = 512)
        String description,
        @NotNull
        EventType eventType,
        @PositiveOrZero
        Integer maxParticipants,
        @NotNull
        @Positive
        int durationMinutes,
        int bufferBeforeMinutes,
        int bufferAfterMinutes) {
}