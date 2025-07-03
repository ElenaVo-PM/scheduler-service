// adapters/web/profile/ProfileController.java
package com.example.scheduler.adapters.web.profile;

import com.example.scheduler.adapters.dto.ProfileResponse;
import com.example.scheduler.application.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> me() {
        return ResponseEntity.ok(service.getProfile()); //TODO Andrey
    }
}
