package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.models.dtos.PasswordForgotRequest;
import com.springbite.authorization_server.models.dtos.PasswordResetRequest;
import com.springbite.authorization_server.services.PasswordRecoveryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
