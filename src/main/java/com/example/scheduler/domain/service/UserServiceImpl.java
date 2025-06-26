package com.example.scheduler.domain.service;

import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
