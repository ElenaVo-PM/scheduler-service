package com.example.scheduler.adapters.web.user;

import com.example.scheduler.adapters.dto.AuthRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.adapters.dto.RefreshTokenRequest;
import com.example.scheduler.adapters.dto.RegisterRequest;
import com.example.scheduler.application.service.AuthService;
import com.example.scheduler.application.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(userService.loginUser(request.username(), request.password()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshTokens(@RequestBody RefreshTokenRequest request) {
        return  ResponseEntity.ok(authService.refreshTokens(request.token()));
    }
}