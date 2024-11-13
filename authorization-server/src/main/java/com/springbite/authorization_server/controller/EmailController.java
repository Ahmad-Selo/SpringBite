package com.springbite.authorization_server.controller;

import com.springbite.authorization_server.exception.CodeExpiredException;
import com.springbite.authorization_server.exception.CodeInvalidException;
import com.springbite.authorization_server.model.dto.SendEmailCodeRequestDTO;
import com.springbite.authorization_server.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/email")
public class EmailController {

    private final MailService mailService;

    @PostMapping("/send-code")
    public ResponseEntity<?> sendEmailCode(
            @Valid @RequestBody SendEmailCodeRequestDTO sendEmailCodeRequestDTO
    ) throws MessagingException {
        return mailService.sendEmailCode(sendEmailCodeRequestDTO);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmEmail(
            @RequestParam("code") String code,
            HttpServletRequest request
    ) throws CodeExpiredException, CodeInvalidException {
        return mailService.confirmEmail(code, request);
    }
}
