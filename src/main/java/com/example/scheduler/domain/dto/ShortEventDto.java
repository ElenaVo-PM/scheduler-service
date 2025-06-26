package com.example.scheduler.domain.dto;

import java.util.UUID;

public record ShortEventDto(
        UUID id,
        String title,
        boolean isActive,
        String link,
        boolean isGroupEvent
) {
}
