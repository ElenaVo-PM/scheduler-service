package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.RegisterRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public UserService(UserRepository userRepo, PasswordEncoder encoder, AuthenticationManager authenticationManager,
            AuthService authService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    public User registerUser(RegisterRequest request) throws IllegalArgumentException {

        Optional<User> dbUser = userRepo.findByUsername(request.username());

        if (dbUser.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        String encodedPassword = encoder.encode(request.password());

        return userRepo.save(request.username(), encodedPassword, request.email());
    }

    public AuthResponse loginUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Credential userDetails = (Credential) authentication.getPrincipal();
        return authService.creteTokens(userDetails);
    }
}
