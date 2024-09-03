package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.models.dtos.SignupWithProviderRequest;
import com.springbite.authorization_server.models.dtos.UserDto;
import com.springbite.authorization_server.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserDto dto,
            HttpServletRequest request
    ) {
        return authService.signup(dto, request);
    }

    @PostMapping("/signup/{provider}")
    public ResponseEntity<?> signupWithProvider(
            @Valid @RequestBody SignupWithProviderRequest signupWithProviderRequest,
            @PathVariable("provider") String provider,
            HttpServletRequest request
    ) {
        return authService.signupWithProvider(signupWithProviderRequest, provider, request);
    }

    @PostMapping("/auth/{provider}")
    public ResponseEntity<?> auth(
            @PathVariable("provider") String provider,
            HttpServletRequest request
    ) {
        return authService.auth(provider, request);
    }
}
