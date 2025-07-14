package com.example.scheduler.domain.fixture;

import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.EventType;

import java.time.Instant;
import java.util.UUID;

public final class TestEvents {

    private TestEvents() {
        throw new AssertionError();
    }

    public static Event demo() {
        return new Event(
                UUID.fromString("8840ddd5-e176-46d8-8f1b-babb00d989cd"),
                UUID.fromString("d3e68c3b-2d6d-48a1-a037-99a390e9433e"),
                "Demo",
                "Sprint #42 demo",
                true,
                1,
                60,
                10,
                15,
                EventType.ONE2ONE,
                "b452644a-dba8-427a-8e44-d5c1bc528231",
                Instant.parse("2024-07-01T10:00:00.000000Z"),
                Instant.parse("2024-07-04T17:00:00.000000Z"),
                Instant.parse("2001-02-03T04:05:06.789012Z"),
                Instant.parse("2001-03-04T05:06:07.890123Z")
        );
    }
}
