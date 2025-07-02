package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.PublicEventResponse;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PublicEventService {
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final UserRepository userRepository;

    public PublicEventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public PublicEventResponse getEventBySharedLink (String sharedLink) {
        UUID sharedLinkUUID = UUID.fromString(sharedLink);
        Optional<Event> requiredEvent = eventRepository.getEventBySharedLink(sharedLinkUUID);
        if (requiredEvent.isEmpty() || !requiredEvent.get().isActive()) throw new RuntimeException();
        User eventOwner = userRepository.findById(requiredEvent.get().ownerId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return convertToPublicResponse(requiredEvent.get(),  eventOwner);
    }

    private PublicEventResponse convertToPublicResponse (Event event, User eventOwner) {
                return new PublicEventResponse(event.title(), event.duration(), event.eventType(), eventOwner.userTimeZone());
    }
}
