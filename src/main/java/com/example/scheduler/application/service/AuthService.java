package com.example.scheduler.application.service;

import com.example.scheduler.adapters.dto.AuthResponse;
import com.example.scheduler.domain.exception.InvalidTokenException;
import com.example.scheduler.domain.repository.JwtRepository;
import com.example.scheduler.infrastructure.security.JwtTokenProvider;
import com.example.scheduler.infrastructure.security.ExtendedUserDetails;
import com.example.scheduler.infrastructure.security.UserDetailsServiceImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtRepository tokenRepository;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public AuthService(JwtRepository tokenRepository, JwtTokenProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.tokenRepository = tokenRepository;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse creteTokens(ExtendedUserDetails userDetails) {
        return createPair(userDetails);
    }

    public AuthResponse refreshTokens(String refreshToken) {
        String username = tokenProvider.getUsernameFromRefreshToken(refreshToken);
        ExtendedUserDetails userDetails = (ExtendedUserDetails) userDetailsService.loadUserByUsername(username);

        if (!tokenProvider.isRefreshTokenValid(refreshToken) ||
                !tokenRepository.contains(userDetails.getId(), refreshToken)) {
            throw new InvalidTokenException(refreshToken);
        }

        return createPair(userDetails);
    }

    private AuthResponse createPair(ExtendedUserDetails userDetails) {
        String accessToken = tokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = createRefreshToken(userDetails);

        return new AuthResponse(accessToken, newRefreshToken);
    }

    private String createRefreshToken(ExtendedUserDetails userDetails) {
        String refreshToken = tokenProvider.generateRefreshToken(userDetails);
        tokenRepository.deleteByUserId(userDetails.getId());
        tokenRepository.save(userDetails.getId(), refreshToken);
        return refreshToken;
    }
}
