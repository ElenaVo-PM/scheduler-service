package com.example.scheduler.infrastructure.security;

import com.example.scheduler.domain.model.User;
import com.example.scheduler.domain.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class ExtendedUserDetails implements UserDetails {
    private UUID id;
    private String username;
    private String password;
    private Set<UserRole> roles;

    private ExtendedUserDetails() {
    }

    public static ExtendedUserDetails build(User user) {
        ExtendedUserDetails details = new ExtendedUserDetails();
        details.id = user.id();
        details.username = user.publicUserName();
        details.password = user.password();
        details.roles = user.roles();
        return details;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
}