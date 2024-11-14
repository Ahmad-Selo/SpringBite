package com.springbite.authorization_server.security.authentication.converter;

import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.exception.UserNotFoundException;
import com.springbite.authorization_server.model.SecurityUser;
import com.springbite.authorization_server.repository.UserRepository;
import com.springbite.authorization_server.security.authentication.CustomAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationConverter implements Converter<Jwt, CustomAuthentication> {

    private final UserRepository userRepository;

    @Override
    public CustomAuthentication convert(Jwt source) {
        List<GrantedAuthority> authorities = extractAuthorities(source);

        Long userId = source.getClaim("uid");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        SecurityUser securityUser = new SecurityUser(user);

        return new CustomAuthentication(source, authorities, securityUser);
    }

    private List<GrantedAuthority> extractAuthorities(Jwt source) {
        Collection<String> authorities = source.getClaim("authorities");

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
