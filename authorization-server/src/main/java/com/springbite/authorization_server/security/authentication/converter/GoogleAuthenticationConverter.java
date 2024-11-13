package com.springbite.authorization_server.security.authentication.converter;

import com.springbite.authorization_server.security.Role;
import com.springbite.authorization_server.security.authentication.GoogleAuthentication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleAuthenticationConverter implements Converter<Jwt, GoogleAuthentication> {

    @Override
    public GoogleAuthentication convert(Jwt source) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(Role.ROLE_GOOGLE.toString()));

        String username = source.getClaim("email");

        return new GoogleAuthentication(source, authorities, username);
    }
}
