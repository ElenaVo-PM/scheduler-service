package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Event;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository {
    Event save(Event event);

    Event regenerateSlug(UUID id);

    Optional<Event> getEventById(UUID id);

    Event toggleActiveEvent(UUID id);
}
