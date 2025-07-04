package com.example.scheduler.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
class TestSecurityConfig {

    @Primary
    @Bean("MockUserDetailsService")
    public UserDetailsService userDetailsService() {
        TestUserDetails user = new TestUserDetails();
        return _ -> user;
    }
}