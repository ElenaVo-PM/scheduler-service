package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.user.RegisterRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.adapters.dto.user.UserDto;

public interface UserService {
    UserDto registerUser(RegisterRequest request);

    AuthResponse loginUser(String username, String password);
}
