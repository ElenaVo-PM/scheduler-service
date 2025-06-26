package com.example.scheduler.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("event_templates")
public record Event(
    @Id
    UUID id,

    @Column("user_id")
    UUID ownerId,

    @Column("title")
    String title,

    @Column("description")
    String description,

    @Column("duration_minutes")
    int duration,

    @Column("buffer_before_minutes")
    int bufferBefore,

    @Column("buffer_after_minutes")
    int bufferAfterInMinutes,

    @Column("is_group_event")
    boolean isGroupEvent,

    @Column("max_participants")
    int maxParticipants,

    @Column("is_active")
    boolean isActive,

    @Column("slug")
    String link,

    @Column("start_date")
    LocalDateTime startDate,

    @Column("end_date")
    LocalDateTime endDate,

    @Column("created_at")
    Instant createdAt,

    @Column("updated_at")
    Instant updatedAt
) {}
