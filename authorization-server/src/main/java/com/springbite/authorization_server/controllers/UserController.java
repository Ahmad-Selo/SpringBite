package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.models.dtos.ChangePasswordRequest;
import com.springbite.authorization_server.models.dtos.DeleteUserRequest;
import com.springbite.authorization_server.models.dtos.UpdateUserRequest;
import com.springbite.authorization_server.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/accounts")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("#userId == authentication.principal.user.id or hasRole('ADMIN')")
    @GetMapping("/{user-id}")
    public ResponseEntity<?> getUser(
            @PathVariable("user-id") Long userId
    ) {
        return userService.getUser(userId);
    }

    @PreAuthorize("#userId == authentication.principal.user.id")
    @PutMapping("/{user-id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        return userService.changePassword(userId, changePasswordRequest);
    }

    @PreAuthorize("#userId == authentication.principal.user.id")
    @PatchMapping("/{user-id}/update")
    public ResponseEntity<?> updateUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        return userService.updateUser(userId, updateUserRequest);
    }

    @PreAuthorize("#userId == authentication.principal.user.id")
    @DeleteMapping("/{user-id}/delete")
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