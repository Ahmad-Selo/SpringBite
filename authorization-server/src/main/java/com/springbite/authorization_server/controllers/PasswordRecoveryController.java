package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.models.dtos.PasswordForgotRequest;
import com.springbite.authorization_server.models.dtos.PasswordResetRequest;
import com.springbite.authorization_server.services.PasswordRecoveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;

    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody PasswordForgotRequest passwordForgotRequest
    ) {
        return passwordRecoveryService.forgotPassword(passwordForgotRequest);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(
            @RequestParam("code") String code
    ) {
        return passwordRecoveryService.verifyCode(code);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> restPassword(
            @RequestParam("token") String token,
            @Valid @RequestBody PasswordResetRequest passwordResetRequest
    ) {
        return passwordRecoveryService.restPassword(token, passwordResetRequest);
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
