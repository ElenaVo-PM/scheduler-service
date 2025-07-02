package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    User save(String username, String password, String email);

    Optional<Credential> getCredential(String username);
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);
}