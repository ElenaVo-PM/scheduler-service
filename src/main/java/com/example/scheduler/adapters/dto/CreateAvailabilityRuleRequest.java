package com.example.scheduler.adapters.dto;

import com.example.scheduler.adapters.annotation.TimeRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@TimeRange(startField = "startTime", endField = "endTime")
public record CreateAvailabilityRuleRequest(
        @NotNull
        @Min(0)
        @Max(6)
        Integer weekday,
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime) {
}