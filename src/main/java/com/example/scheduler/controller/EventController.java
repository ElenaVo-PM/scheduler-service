package com.example.scheduler.controller;

import com.example.scheduler.domain.dto.ShortEventDto;
import com.example.scheduler.domain.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    List<ShortEventDto> getAllEventsForCurrentUser(Principal principal) {
        return eventService.getAllEventsForCurrentUser(principal.getName());
    }
}
