package com.example.scheduler.adapters.dto;

import com.example.scheduler.domain.model.EventType;

import java.util.TimeZone;

public record PublicEventResponse(
        String title,
        int duration,
        EventType groupEvent,
        TimeZone timeZone
) {
}
