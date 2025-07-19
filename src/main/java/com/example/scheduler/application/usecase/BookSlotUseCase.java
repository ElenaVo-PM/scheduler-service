package com.example.scheduler.application.usecase;

import com.example.scheduler.adapters.dto.BookingRequest;
import com.example.scheduler.adapters.dto.BookingResponse;
import com.example.scheduler.application.service.UserService;
import com.example.scheduler.domain.exception.NotFoundException;
import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.Event;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.EventRepository;
import com.example.scheduler.domain.repository.SlotRepository;
import com.example.scheduler.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookSlotUseCase {

    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public BookSlotUseCase(SlotRepository repository,
                           UserRepository userRepository,
                           EventRepository eventRepository) {
        this.slotRepository = repository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public BookingResponse execute(BookingRequest request) throws IllegalAccessException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Event event = eventRepository.getEventById(request.eventId())
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Credential credential = (Credential) auth.getCredentials();
            User user = userRepository.findByUsername(credential.getUsername())
                    .orElseThrow();

            return slotRepository.bookSlot(event, user, request);
        }

        return slotRepository.bookSlot(event, request);
    }

}
