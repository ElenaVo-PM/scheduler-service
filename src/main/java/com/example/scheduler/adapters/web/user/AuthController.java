package com.example.scheduler.adapters.web.user;

import com.example.scheduler.adapters.dto.AuthRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.adapters.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return null;
    }
}