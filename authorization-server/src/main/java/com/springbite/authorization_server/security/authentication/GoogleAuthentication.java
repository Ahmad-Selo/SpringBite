package com.springbite.authorization_server.security.authentication;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
public class GoogleAuthentication extends JwtAuthenticationToken {

    private final String username;

    public GoogleAuthentication(
            Jwt jwt,
            Collection<? extends GrantedAuthority> authorities,
            String username
    ) {
        super(jwt, authorities);
        this.username = username;
    }
}
