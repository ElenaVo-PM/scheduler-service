package com.example.scheduler.domain.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Credential implements UserDetails {
    private final UUID userId;
    private final String username;
    private final String password;
    private final UserRole role;
    private final boolean enabled;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Credential(UUID userId,
                      String username,
                      String password,
                      String role,
                      boolean enabled,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = UserRole.valueOf(role);
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Credential that)) return false;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
