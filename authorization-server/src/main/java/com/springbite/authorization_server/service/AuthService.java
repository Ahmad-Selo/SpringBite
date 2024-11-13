package com.springbite.authorization_server.service;

import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.mapper.UserMapper;
import com.springbite.authorization_server.model.SecurityUser;
import com.springbite.authorization_server.model.dto.UserRequestDTO;
import com.springbite.authorization_server.repository.UserRepository;
import com.springbite.authorization_server.security.Auth;
import com.springbite.authorization_server.security.RandomGenerator;
import com.springbite.authorization_server.security.Role;
import com.springbite.authorization_server.security.authentication.GoogleAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final Auth auth;

    public ResponseEntity<?> signup(
            UserRequestDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        dto.setPassword(encodedPassword);

        User user = userMapper.userDtoToUser(dto, false, false);

        User savedUser = userRepository.save(user);

        SecurityUser securityUser = userMapper.userToSecurityUser(savedUser);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                securityUser.getAuthorities()
        );

        auth.login(authentication, request, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections
                .singletonMap("message", "Signup successfully")
        );
    }

    public ResponseEntity<?> providerAuth(
            Authentication providerAuthentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication;

        authentication = googleAuth((GoogleAuthentication) providerAuthentication);

        auth.login(authentication, request, response);

        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap(
                "message", "Authenticated successfully"
        ));
    }

    public Authentication googleAuth(GoogleAuthentication authentication) {
        UsernamePasswordAuthenticationToken authenticationToken;

        String username = authentication.getUsername();
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if(user != null) {
            SecurityUser securityUser = userMapper.userToSecurityUser(user);

            authenticationToken = new UsernamePasswordAuthenticationToken(
                    securityUser,
                    null,
                    securityUser.getAuthorities()
            );

            return authenticationToken;
        }

        String firstname = (String) authentication.getTokenAttributes().get("given_name");
        String lastname = (String) authentication.getTokenAttributes().get("family_name");
        Boolean emailVerified = (Boolean) authentication.getTokenAttributes().get("email_verified");
        String picture = (String) authentication.getTokenAttributes().get("picture");
        String password = passwordEncoder.encode(RandomGenerator.password(16));

        user = new User(
                username,
                password,
                firstname,
                lastname,
                null,
                picture,
                Role.ROLE_GOOGLE,
                emailVerified,
                false,
                true,
                true
        );

        User savedUser = userRepository.save(user);

        SecurityUser securityUser = userMapper.userToSecurityUser(savedUser);

        authenticationToken = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                securityUser.getAuthorities()
        );

        return authenticationToken;
    }
}
