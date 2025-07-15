package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventFullDto;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.domain.model.Credential;

import java.util.UUID;

public interface EventService {

    EventResponse createEvent(CreateEventRequest request, UUID ownerId);

    EventResponse refreshSlug(UUID eventId);

    EventFullDto getEventById(String email, UUID eventId);

    EventResponse toggleActiveEvent(UUID id);

    EventFullDto getEventById(UUID userId, UUID eventId, Credential currentUser);

    void updateEvent(UUID id, CreateEventRequest request);
}
