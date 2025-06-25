package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.infrastructure.security.ExtendedUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    public UserService(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }


    public User registerUser(String username, String password, String email) {
        return null;
    }

    public AuthResponse loginUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ExtendedUserDetails userDetails = (ExtendedUserDetails) authentication.getPrincipal();
        return authService.creteTokens(userDetails);
    }
}