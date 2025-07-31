package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.PublicEventResponse;
import com.example.scheduler.application.service.PublicEventService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/public/event")
public class PublicEventController {
    private final PublicEventService publicEventService;

    public PublicEventController (PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping("/{sharedLink}")
    public PublicEventResponse getEventBySharedLink(@PathVariable UUID sharedLink) {
        return publicEventService.getEventBySlug(sharedLink);
    }
}
