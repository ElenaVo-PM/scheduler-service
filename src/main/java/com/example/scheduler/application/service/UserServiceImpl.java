package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.user.RegisterRequest;
import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.adapters.dto.user.UserDto;
import com.example.scheduler.adapters.mapper.UserMapper;
import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final Clock clock;

    public UserServiceImpl(
            UserRepository userRepo,
            UserMapper userMapper,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager,
            AuthService authService,
            Clock clock
    ) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.clock = clock;
    }

    @Override
    public UserDto registerUser(RegisterRequest request) {
        Objects.requireNonNull(request, "request cannot be null");
        User prepared = createUser(request, encoder, clock);
        User registered = userRepo.insert(prepared);
        log.info("Registered new user [{}]", registered.id());
        log.debug("User registered = {}", registered);
        return userMapper.toDto(registered);
    }

    @Override
    public AuthResponse loginUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Credential userDetails = (Credential) authentication.getPrincipal();
        return authService.creteTokens(userDetails);
    }

    private User createUser(RegisterRequest registerRequest, PasswordEncoder passwordEncoder, Clock clock) {
        String passwordHash = passwordEncoder.encode(registerRequest.password());
        Instant now = Instant.now(clock);
        return User.builder()
                .id(UUID.randomUUID())
                .username(registerRequest.username())
                .email(registerRequest.email())
                .passwordHash(passwordHash)
                .role("USER")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
