package com.example.scheduler.infrastructure.security;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Credential loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getCredential(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
