package com.example.scheduler.domain.dto;

import java.time.Instant;

public record ShortEventDto() {
    Long id,
    String title,
    boolean isActive,
}

ID, title, active, shareLink, groupEvent