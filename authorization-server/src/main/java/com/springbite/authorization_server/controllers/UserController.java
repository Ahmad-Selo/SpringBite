package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.models.dtos.*;
import com.springbite.authorization_server.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

@RestController
public class UserController {

    private final Logger logger = Logger.getLogger(
            UserController.class.getName()
    );

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody UserDto dto,
            HttpServletRequest request) {
        return userService.signup(dto, request);
    }

    @PostMapping("/signup/{provider}")
    public ResponseEntity<?> signupWithProvider(
            @NotNull @RequestBody UserDto dto,
            @PathVariable("provider") String provider,
            HttpServletRequest request
    ) {
        return userService.signupWithProvider(dto, provider, request);
    }

    @PostMapping("/auth/{provider}")
    public ResponseEntity<?> auth(
            @PathVariable("provider") String provider,
            @NotNull @RequestParam("scope") String scope,
            HttpServletRequest request) {
        return userService.auth(provider, scope, request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest
    ) {
        return userService.forgotPassword(forgotPasswordRequest);
    }

    @PreAuthorize("#userId == authentication.principal.user.id")
    @PutMapping("/accounts/{user-id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        return userService.changePassword(userId, changePasswordRequest);
    }

    @PreAuthorize("#userId == authentication.principal.user.id")
    @PatchMapping("/accounts/{user-id}/update")
    public ResponseEntity<?> updateUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        return userService.updateUserDetails(userId, updateUserRequest);
    }

    @PreAuthorize("#userId == authentication.principal.user.id")
    @DeleteMapping("/accounts/{user-id}/delete")
    public ResponseEntity<?> deleteUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody DeleteUserRequest deleteUserRequest
    ) {
        return userService.deleteUser(userId, deleteUserRequest);
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", "Missing body."));
    }
}