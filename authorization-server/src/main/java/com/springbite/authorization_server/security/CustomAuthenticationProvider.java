package com.springbite.authorization_server.security;

import com.springbite.authorization_server.exceptions.EmailNotVerifiedException;
import com.springbite.authorization_server.models.SecurityUser;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, securityUser.getPassword())) {
            throw new BadCredentialsException("Incorrect Password.");
        }

        if (!securityUser.getUser().isEmailVerified()) {
            throw new EmailNotVerifiedException("Please verify your email to continue.");
        }

        return new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                securityUser.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication
                .equals(UsernamePasswordAuthenticationToken.class);
    }
}
