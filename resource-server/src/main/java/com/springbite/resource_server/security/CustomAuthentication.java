package com.springbite.resource_server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomAuthentication extends JwtAuthenticationToken {

    private final Long userId;

    private final boolean emailVerified;

    public CustomAuthentication(
            Jwt jwt,
            Collection<? extends GrantedAuthority> authorities,
            Long userId,
            boolean emailVerified
    ) {
        super(jwt, authorities);

        this.userId = userId;
        this.emailVerified = emailVerified;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }
}
