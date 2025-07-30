package com.example.scheduler.domain.model;

import java.time.Instant;

public class TimeInterval {
    Instant startTime;
    Instant endTime;

    public boolean overlapsWithSlot(Slot slot) {
        return this.startTime.isBefore(slot.endTime()) && slot.startTime().isBefore(this.endTime);
    }
}
