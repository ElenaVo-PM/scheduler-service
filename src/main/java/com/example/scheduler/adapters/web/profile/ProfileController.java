package com.example.scheduler.adapters.web.profile;

import com.example.scheduler.adapters.dto.ProfileResponse;
import com.example.scheduler.application.service.ProfileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            @RequestHeader("X-USER-ID") UUID userId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        return ResponseEntity.ok(service.getProfile(userId, authorization));
    }
}