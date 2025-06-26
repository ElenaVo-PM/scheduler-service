package com.example.scheduler.domain.model;

import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;

@Table("event_templates")
public record Event(
    Long id,
    Long ownerId,
    String title,
    String description,
    Integer durationInMinutes,
    Integer bufferBeforeInMinutes,
    Integer bufferAfterInMinutes,
    boolean isGroupEvent,
    int maxParticipants,
    boolean isActive,
    String eventType,
    String link,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Instant createdAt,
    Instant updatedAt
) {}
