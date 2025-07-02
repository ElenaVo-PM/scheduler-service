package com.example.scheduler.adapters.web.event;

import com.example.scheduler.adapters.dto.PublicEventResponse;
import com.example.scheduler.application.service.PublicEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicEventController {
    @Autowired
    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping("/events/{sharedLink}")
    public ResponseEntity<PublicEventResponse> getEventBySharedLink(@PathVariable String sharedLink) {
    PublicEventResponse requiredEvent = publicEventService.getEventBySharedLink(sharedLink);
    return ResponseEntity.ok(requiredEvent);
 }
}
