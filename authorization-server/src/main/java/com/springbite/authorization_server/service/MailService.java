package com.springbite.authorization_server.service;

import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.entity.VerificationCode;
import com.springbite.authorization_server.exception.CodeExpiredException;
import com.springbite.authorization_server.exception.CodeInvalidException;
import com.springbite.authorization_server.exception.UserNotFoundException;
import com.springbite.authorization_server.model.dto.SendEmailCodeRequestDTO;
import com.springbite.authorization_server.repository.ConfirmationCodeRepository;
import com.springbite.authorization_server.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;
    private final ConfirmationCodeRepository confirmationCodeRepository;

    @Value("${mail.from}")
    private String from;
    @Value("${mail.support}")
    private String support;
    @Value("${website.url}")
    private String websiteUrl;

    public ResponseEntity<?> sendEmailCode(SendEmailCodeRequestDTO sendEmailCodeRequestDTO)
            throws MessagingException {
        User user = userRepository.findByUsername(sendEmailCodeRequestDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Username not found"));

        if (user.isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.OK).body(Collections
                    .singletonMap("message", "Your email address has already been verified. No further action is required."));
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<VerificationCode> userVerificationCodes = confirmationCodeRepository.findByUser(user, sort)
                .orElse(new ArrayList<>());

        if (userVerificationCodes.size() >= 3) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Collections
                    .singletonMap("message", "You have reached your daily limit for confirmation code requests. Please try again tomorrow or contact our support team for further assistance"));
        }

        if (!userVerificationCodes.isEmpty() && userVerificationCodes.getLast().getExpiresAt().getTime() > System.currentTimeMillis()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Collections
                    .singletonMap("message", "You've recently requested a code. Please wait 15 minutes from your last request before trying again. If you continue to experience issues, feel free to contact our support team for assistance"));
        }

        VerificationCode verificationCode = new VerificationCode(user);

        sendConfirmationEmail(user.getUsername(), user.getFirstname(), verificationCode.getCode());

        confirmationCodeRepository.save(verificationCode);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "A confirmation email has been sent"));
    }

    public ResponseEntity<?> confirmEmail(String code, HttpServletRequest request)
            throws CodeInvalidException, CodeExpiredException {
        VerificationCode verificationCode = confirmationCodeRepository.findByCode(code)
                .orElseThrow(() -> new CodeInvalidException("Invalid code"));

        verificationCode.validateCode();

        User user = verificationCode.getUser();

        user.setEmailVerified(true);

        verificationCode.setExpired(true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Your email has been successfully confirmed. You can now access all features of your account"));
    }

    @Async
    public void sendConfirmationEmail(String to, String firstname, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("code", code);
        context.setVariable("supportEmail", support);
        context.setVariable("websiteUrl", websiteUrl);

        String emailContent = templateEngine.process("confirmation-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(from);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Confirm Your Registration with Spring Bite");
        message.setContent(emailContent, "text/html");

        mailSender.send(message);
    }

    @Async
    public void sendPasswordResetEmail(String to, String firstname, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("code", code);
        context.setVariable("supportEmail", support);
        context.setVariable("websiteUrl", websiteUrl);

        String emailContent = templateEngine.process("password-reset-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(from);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Reset Your Password");
        message.setContent(emailContent, "text/html");

        mailSender.send(message);
    }
}
