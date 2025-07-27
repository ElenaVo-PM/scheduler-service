package com.example.scheduler.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Booking {
    UUID id;
    UUID eventTemplateId;
    String inviteeName;
    String inviteeEmail;
    Instant startTime;
    Instant endTime;
    boolean isCanceled;
    Instant createdAt;

    public UUID getId() {
        return id;
    }

    public UUID getEventTemplateId() {
        return eventTemplateId;
    }

    public String getInviteeName() {
        return inviteeName;
    }

    public String getInviteeEmail() {
        return inviteeEmail;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
