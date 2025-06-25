package com.example.scheduler.adapters.mapper;

import com.example.scheduler.adapters.dto.ProfileResponse;
import com.example.scheduler.domain.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public ProfileResponse toDto(Profile profile, String username) {
        return new ProfileResponse(profile.userId(),
                username,
                profile.description(),
                profile.isActive(),
                profile.logo(),
                profile.createdAt(),
                profile.updatedAt()
        );
    }
}