package com.example.scheduler.adapters.web.user;

import com.example.scheduler.adapters.dto.AuthRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.adapters.dto.RefreshTokenRequest;
import com.example.scheduler.adapters.dto.user.RegisterRequest;
import com.example.scheduler.adapters.dto.user.UserDto;
import com.example.scheduler.application.service.AuthService;
import com.example.scheduler.application.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Valid RegisterRequest request) {
        log.info("Received request to register user: username = {}", request.username());
        log.debug("Register request = {}", request);
        UserDto response = userService.registerUser(request);
        log.info("Responded with user registered: id = [{}], username = {}", response.id(), response.username());
        log.debug("User registered = {}", response);
        return response;
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
