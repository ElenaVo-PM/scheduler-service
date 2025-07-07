package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.RegisterRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.domain.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(RegisterRequest request);

    AuthResponse loginUser(String username, String password);

    Optional<User> getUser(String email);
}
