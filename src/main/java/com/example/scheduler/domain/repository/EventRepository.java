package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    Event save(Event event);

    void update(Event event);

    Event regenerateSlug(UUID id);

    Optional<Event> getEventById(UUID id);

    List<Event> getAllEvents(UUID ownerId);
}
