package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventFullDto;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.infrastructure.mapper.EventMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(UserService userService, EventRepository eventRepository, EventMapper eventMapper) {
        this.userService = userService;
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
    @Transactional(readOnly = true)
    public EventFullDto getEventById(String email, UUID eventId) {
        User user = userService.getUser(email)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));

        Event event = eventRepository.getEventById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено."));

        if (!event.ownerId().equals(user.id())) {
            throw new AccessDeniedException("Доступ запрещён!");
        }

        return eventMapper.toEventFullDto(event, user);
    }
}

