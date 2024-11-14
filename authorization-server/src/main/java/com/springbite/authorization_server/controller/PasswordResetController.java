package com.springbite.authorization_server.controller;

import com.springbite.authorization_server.exception.CodeExpiredException;
import com.springbite.authorization_server.exception.CodeInvalidException;
import com.springbite.authorization_server.exception.TokenExpiredException;
import com.springbite.authorization_server.exception.TokenInvalidException;
import com.springbite.authorization_server.model.dto.PasswordForgotRequestDTO;
import com.springbite.authorization_server.model.dto.PasswordResetRequestDTO;
import com.springbite.authorization_server.service.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody PasswordForgotRequestDTO passwordForgotRequestDTO
    ) throws MessagingException {
        return passwordResetService.forgotPassword(passwordForgotRequestDTO);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(
            @RequestParam("code") String code
    ) throws CodeExpiredException, CodeInvalidException {
        return passwordResetService.verifyCode(code);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @Valid @RequestBody PasswordResetRequestDTO passwordResetRequestDTO
    ) throws TokenInvalidException, TokenExpiredException {
        return passwordResetService.resetPassword(token, passwordResetRequestDTO);
    }
}
