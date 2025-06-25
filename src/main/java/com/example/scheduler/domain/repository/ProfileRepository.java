package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository {
    Optional<Profile> findById(UUID userId);
}