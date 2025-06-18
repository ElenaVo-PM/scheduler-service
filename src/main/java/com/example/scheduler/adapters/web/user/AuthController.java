package com.example.scheduler.adapters.web.user;

import com.example.scheduler.adapters.dto.AuthRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.adapters.dto.RegisterRequest;
import com.example.scheduler.application.service.UserService;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.model.UserDetail;
import com.example.scheduler.infrastructure.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {

        try {
            userService.registerUser(request.username(), request.password(), request.email());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetail user = (UserDetail) auth.getPrincipal();

        String token = tokenProvider.generateToken(user.getUser());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}