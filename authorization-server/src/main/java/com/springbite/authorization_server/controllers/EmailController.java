package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.models.dtos.SendEmailCodeRequest;
import com.springbite.authorization_server.services.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendEmailCode(
            @Valid @RequestBody SendEmailCodeRequest sendEmailCodeRequest
    ) {
        return emailService.sendEmailCode(sendEmailCodeRequest);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmEmail(
            @RequestParam("code") String code,
            HttpServletRequest request
    ) {
        return emailService.confirmEmail(code, request);
    }
}
