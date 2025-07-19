package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.domain.exception.InvalidTokenException;
import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.repository.JwtRepository;
import com.example.scheduler.infrastructure.security.JwtTokenProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtRepository tokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public AuthService(JwtRepository tokenRepository,
                       JwtTokenProvider tokenProvider,
                       UserDetailsService userDetailsService) {
        this.tokenRepository = tokenRepository;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse createTokens(Credential userDetails) {
        return createPair(userDetails);
    }

    public AuthResponse refreshTokens(String refreshToken) {
        String username = tokenProvider.getUsernameFromRefreshToken(refreshToken);
        Credential userDetails = (Credential) userDetailsService.loadUserByUsername(username);

        if (!tokenProvider.isRefreshTokenValid(refreshToken) ||
                !tokenRepository.contains(userDetails.getId(), refreshToken)) {
            throw new InvalidTokenException(refreshToken);
        }

        return createPair(userDetails);
    }

    private AuthResponse createPair(Credential userDetails) {
        String accessToken = tokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = createRefreshToken(userDetails);

        return new AuthResponse(accessToken, newRefreshToken);
    }

    private String createRefreshToken(Credential userDetails) {
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        tokenRepository.deleteByUserId(userDetails.getId());
        tokenRepository.save(userDetails.getId(), refreshToken);
        return refreshToken;
    }
}
