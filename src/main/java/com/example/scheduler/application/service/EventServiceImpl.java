package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventFullDto;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.domain.exception.NotEnoughAuthorityException;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.exception.UserNotAuthorizedException;
import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.infrastructure.mapper.EventMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventResponse createEvent(CreateEventRequest request, UUID ownerId) {
        Event requestEvent = eventMapper.toEntity(request, ownerId);
        Event savedEvent = eventRepository.save(requestEvent);
        return eventMapper.toResponse(savedEvent);
    }

    @Override
    @PreAuthorize("@security.isOwner(#eventId)")
    public EventResponse refreshSlug(UUID eventId) {
        Event updatedEvent = eventRepository.regenerateSlug(eventId);

        return eventMapper.toResponse(updatedEvent);
    }

    @Override
    public EventFullDto getEventById(UUID userId, UUID eventId, Credential currentUser) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(eventId, "eventId cannot be null");
        requireUserIdMatchCurrentUser(userId, currentUser);
        Event event = eventRepository.getEventById(eventId).orElseThrow(
                () -> new NotFoundException("Event [%s] not found".formatted(eventId))
        );
        requireUserIsEventOwner(userId, event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @PreAuthorize("@security.isOwner(#eventId)")
    public void updateEvent(UUID eventId, CreateEventRequest request) {
        Event preUpdatedEvent = eventRepository.getEventById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено."));

        Event updatedEvent = eventMapper.updateEntityFromDto(preUpdatedEvent, request);
        eventRepository.update(updatedEvent);
    }

    private void requireUserIdMatchCurrentUser(UUID userId, Credential currentUser) {
        if (currentUser == null) {
            throw new UserNotAuthorizedException("User [%s] is not authorized".formatted(userId));
        } else if (!userId.equals(currentUser.getId())) {
            throw new NotEnoughAuthorityException("User can get own events only");
        }
    }

    private void requireUserIsEventOwner(UUID userId, Event event) {
        if (!userId.equals(event.ownerId())) {
            throw new NotEnoughAuthorityException("User can get own events only");
        }
    }

    @PreAuthorize("@security.isOwner(#eventId)")
    public EventResponse toggleActiveEvent(UUID eventId) {
        Event updatedEvent = eventRepository.toggleActiveEvent(eventId);

        return eventMapper.toResponse(updatedEvent);
    }

    @PreAuthorize("@security.isOwner(#eventId)")
    public EventResponse toggleActiveEvent(UUID eventId) {
        Event updatedEvent = eventRepository.toggleActiveEvent(eventId);

        return eventMapper.toResponse(updatedEvent);
    }
}

