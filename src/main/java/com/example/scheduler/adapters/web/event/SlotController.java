package com.example.scheduler.adapters.web.event;

import com.example.scheduler.application.service.SlotService;
import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/slots")
public class SlotController {
    private final SlotService slotService;

    @Autowired
    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    public List<Slot> getAvailableSlots(@RequestParam UUID eventId, @AuthenticationPrincipal Credential credential) {
        return slotService.getAvailableSlots(eventId, credential.getId());
    }
}
