package com.example.scheduler.domain.model;

import java.util.TimeZone;
import java.util.UUID;

public record UserGeneralInfo(
        UUID id,
        String username,
        String email,
        String fullName,
        TimeZone timezone) {
}
