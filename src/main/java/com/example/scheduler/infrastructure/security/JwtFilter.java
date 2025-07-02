package com.example.scheduler.infrastructure.security;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.infrastructure.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtTokenProvider provider;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtTokenProvider provider, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.provider = provider;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtUtil.extractTokenFromRequest(request);
        if (Objects.nonNull(accessToken) && provider.isAccessTokenValid(accessToken)) {
            String username = provider.getUsernameFromAccessToken(accessToken);

            Credential userDetails = (Credential) userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}