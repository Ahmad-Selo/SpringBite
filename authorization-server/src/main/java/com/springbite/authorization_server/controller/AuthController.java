package com.springbite.authorization_server.controller;

import com.springbite.authorization_server.model.dto.UserRequestDTO;
import com.springbite.authorization_server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserRequestDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.signup(dto, request, response);
    }

    @PostMapping("/auth/{provider}")
    public ResponseEntity<?> providerAuth(
            @PathVariable("provider") String provider,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.providerAuth(authentication, request, response);
    }
}
