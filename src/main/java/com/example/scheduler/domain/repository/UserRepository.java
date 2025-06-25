package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByUsername(String username);

    User save(User user);

    Optional<User> findById(UUID id);
}