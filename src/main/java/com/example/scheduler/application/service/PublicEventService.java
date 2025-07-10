package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.PublicEventResponse;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.UserGeneralInfo;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class PublicEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public PublicEventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PublicEventResponse getEventBySlug (String sharedLink) {
        UUID slugUUID = UUID.fromString(sharedLink);
        Optional<Event> requiredEvent = eventRepository.getEventBySlug(slugUUID);
        if (requiredEvent.isEmpty() || !requiredEvent.get().isActive()) throw new NotFoundException("Событие не найдено");
        UserGeneralInfo eventOwner = userRepository.findUserGeneralInfoById(requiredEvent.get().ownerId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToPublicResponse(requiredEvent.get(),  eventOwner);
    }

    private PublicEventResponse convertToPublicResponse (Event event, UserGeneralInfo eventOwner) {
                return new PublicEventResponse(event.title(), event.durationMinutes(), event.eventType(), eventOwner.timezone());
    }
}
