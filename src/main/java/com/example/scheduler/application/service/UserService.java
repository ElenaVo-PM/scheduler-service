package com.example.scheduler.application.service;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepo,
                       PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public Credential loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> dbUser = userRepo.findByUsername(username);

        if (dbUser.isEmpty()) {
            return null;
        }

        return userRepo.getCredential(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User registerUser(String username, String password, String email) throws IllegalArgumentException {

        Optional<User> dbUser = userRepo.findByUsername(username);

        if (dbUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        String encodedPassword = encoder.encode(password);

        return userRepo.save(username, encodedPassword, email);
    }

}