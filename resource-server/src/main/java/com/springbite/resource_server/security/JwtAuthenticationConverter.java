package com.springbite.resource_server.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, CustomAuthentication> {

    public JwtAuthenticationConverter() {
    }

    @Override
    public CustomAuthentication convert(Jwt source) {
        List<GrantedAuthority> authorities = extractAuthorities(source);

        Long userId = (Long) source.getClaims().get("uid");

        boolean emailVerified = (boolean) source.getClaims().get("email_verified");

        return new CustomAuthentication(source, authorities, userId, emailVerified);
    }

    private List<GrantedAuthority> extractAuthorities(Jwt source) {
        Collection<String> authorities = (Collection<String>) source.getClaims().get("authorities");

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
