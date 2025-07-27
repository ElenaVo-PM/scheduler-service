package com.example.scheduler.adapters.dto;

import java.time.Instant;

public class BookingGeneralInfo {
    String eventName;
    String inviteeName;
    String inviteeEmail;
    Instant startTime;
    Instant endTime;
    boolean isCanceled;
    Instant createdAt;

    public BookingGeneralInfo(String eventName, String inviteeName, String inviteeEmail, Instant startTime, Instant endTime, boolean isCanceled, Instant createdAt) {
        this.eventName = eventName;
        this.inviteeName = inviteeName;
        this.inviteeEmail = inviteeEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isCanceled = isCanceled;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "BookingGeneralInfo{" +
               "eventName='" + eventName + '\'' +
               ", inviteeName='" + inviteeName + '\'' +
               ", inviteeEmail='" + inviteeEmail + '\'' +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               ", isCanceled=" + isCanceled +
               ", createdAt=" + createdAt +
               '}';
    }
}
