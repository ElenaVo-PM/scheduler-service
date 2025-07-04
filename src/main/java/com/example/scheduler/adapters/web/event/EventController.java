package com.example.scheduler.adapters.web.event;


import com.example.scheduler.adapters.dto.EventFullDto;

import com.example.scheduler.adapters.dto.CreateEventRequest;
import com.example.scheduler.adapters.dto.EventResponse;
import com.example.scheduler.application.service.EventService;
import com.example.scheduler.domain.model.Credential;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody @Valid CreateEventRequest request,
                                                     @AuthenticationPrincipal Credential userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.createEvent(request, userDetails.getId()));
    }

    @PostMapping("/{id}/regenerate-link")
    public ResponseEntity<EventResponse> refreshSlug(@PathVariable UUID eventId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.refreshSlug(eventId));

    }

    /**
     * GET /events/{eventId} - Получение конкретного события
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(Principal principal, @PathVariable UUID eventId) {
        EventFullDto event = eventService.getEventById(principal.getName(), eventId);
        return ResponseEntity.ok(event);
    }
}
