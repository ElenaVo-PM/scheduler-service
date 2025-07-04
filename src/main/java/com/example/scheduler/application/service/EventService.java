package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventFullDto;
import com.example.scheduler.adapters.dto.EventResponse;

import java.util.UUID;

public interface EventService {
    EventResponse createEvent(CreateEventRequest request, UUID ownerId);

    EventFullDto getEventById(String email, UUID eventId);
}