package com.springbite.authorization_server.controllers;

import com.nimbusds.jose.JOSEException;
import com.springbite.authorization_server.exceptions.UserAlreadyExistsException;
import com.springbite.authorization_server.models.dtos.UserDto;
import com.springbite.authorization_server.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserDto dto,
            HttpServletRequest request) {
        try {
            return userService.signup(dto, request);
        } catch (UserAlreadyExistsException | JOSEException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/auth/{provider}")
    public ResponseEntity<?> auth(
            @RequestBody UserDto dto,
            @NonNull @PathVariable("provider") String provider,
            @NonNull @RequestParam("client_id") String clientId,
            @NonNull @RequestParam("scope") String scope,
            HttpServletRequest request) {
        try {
            return userService.auth(dto, provider, clientId, scope, request);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}