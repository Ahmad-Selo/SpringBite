package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.models.dtos.SignupWithProviderRequest;
import com.springbite.authorization_server.models.dtos.UserDto;
import com.springbite.authorization_server.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        var errors = new ArrayList<>();

        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", errors));
    }
}
