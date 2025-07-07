package com.example.scheduler.adapters.dto;

public record BookingRequest(
        Long eventId,
        Long slotId
) {
}