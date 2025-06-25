package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.ProfileResponse;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ProfileService {

    public ProfileResponse getProfile(UUID headerUserId, String bearerHeader) {
        return null;
    }
}