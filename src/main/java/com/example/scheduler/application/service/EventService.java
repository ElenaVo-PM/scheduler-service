package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventFullDto;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.domain.model.Credential;

import java.util.UUID;

public interface EventService {

    EventResponse createEvent(CreateEventRequest request, UUID ownerId);

    EventResponse refreshSlug(UUID eventId);

    public EventResponse createEvent(CreateEventRequest request, UUID ownerId) {
        Event requestEvent = eventMapper.toEntity(request, ownerId);
        Event savedEvent = eventRepository.save(requestEvent);
        return eventMapper.toResponse(savedEvent);
    }

    @PreAuthorize("@security.isOwner(#eventId)")
    public EventResponse refreshSlug(UUID eventId) {
        Event updatedEvent = eventRepository.regenerateSlug(eventId);

        return eventMapper.toResponse(updatedEvent);
    }

    @PreAuthorize("@security.isOwner(#eventId)")
    public EventResponse toggleActiveEvent(UUID eventId) {
        Event updatedEvent = eventRepository.toggleActiveEvent(eventId);

        return eventMapper.toResponse(updatedEvent);
    }
    EventFullDto getEventById(String email, UUID eventId);

    EventFullDto getEventById(UUID userId, UUID eventId, Credential currentUser);

    void updateEvent(UUID id, CreateEventRequest request);
}
