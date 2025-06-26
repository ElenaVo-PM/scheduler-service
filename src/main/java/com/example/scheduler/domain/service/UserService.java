package com.example.scheduler.domain.service;

import com.example.scheduler.domain.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUser(String email);

}
