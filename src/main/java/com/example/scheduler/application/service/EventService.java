package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.infrastructure.mapper.EventMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository,
                        EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    public EventResponse createEvent(CreateEventRequest request, UUID ownerId) {
        Event requestEvent = eventMapper.toEntity(request, ownerId);
        Event savedEvent = eventRepository.save(requestEvent);
        return eventMapper.toResponse(savedEvent);
    }
}