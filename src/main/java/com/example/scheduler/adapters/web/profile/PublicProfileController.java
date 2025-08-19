package com.example.scheduler.adapters.web.profile;

import com.example.scheduler.adapters.dto.ProfilePublicDto;
import com.example.scheduler.application.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public/profiles")
public class PublicProfileController {
    private static final Logger log = LoggerFactory.getLogger(PublicProfileController.class);

    private final ProfileService service;

    public PublicProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ProfilePublicDto getPublicProfile(@PathVariable("id") UUID userId) {
        log.info("Received public request for profile id = {}", userId);
        ProfilePublicDto response = service.getPublicProfile(userId);
        log.info("Responded with public profile for user {}", userId);
        log.debug("Public profile = {}", response);
        return response;
    }
}
