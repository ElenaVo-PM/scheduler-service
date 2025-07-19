package com.example.scheduler.domain.model;

import java.time.Instant;
import java.util.UUID;

public record BookedSlot(
        UUID id,
        UUID eventId,
        UUID slotId,
        String inviteeName,
        String inviteeEmail,
        UUID userId,
        Instant startTime,
        Instant endTime,
        boolean isCanceled
) {
}