package com.example.scheduler.infrastructure.config;

import com.example.scheduler.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http, JwtTokenProvider tokenProvider) {
        return null;
    }
}
