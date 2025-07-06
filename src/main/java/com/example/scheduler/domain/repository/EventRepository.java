package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Event;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    Optional<Event> findById(UUID id);

    Event save(Event event);

    Event regenerateSlug(UUID id);
}
