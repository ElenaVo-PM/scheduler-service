package com.example.scheduler.infrastructure.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class TestSecurityConfig {

    @Primary
    @Bean("MockUserDetailsService")
    public UserDetailsService userDetailsService() {
        TestUserDetails user = new TestUserDetails();
        return num -> user;
    }
}
