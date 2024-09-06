package com.springbite.authorization_server.services;

import com.springbite.authorization_server.entities.ConfirmationCode;
import com.springbite.authorization_server.exceptions.CodeExpiredException;
import com.springbite.authorization_server.exceptions.CodeInvalidException;
import com.springbite.authorization_server.mappers.UserMapper;
import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.SendEmailCodeRequest;
import com.springbite.authorization_server.repositories.ConfirmationCodeRepository;
import com.springbite.authorization_server.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final SecurityContextService securityContextService;

    @Value("${email.from}")
    private String fromEmail;
    @Value("${email.support}")
    private String supportEmail;
    @Value("${website.url}")
    private String websiteUrl;

    public EmailService(
            JavaMailSender mailSender,
            TemplateEngine templateEngine,
            UserRepository userRepository,
            UserMapper userMapper,
            ConfirmationCodeRepository confirmationCodeRepository,
            SecurityContextService securityContextService
    ) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.confirmationCodeRepository = confirmationCodeRepository;
        this.securityContextService = securityContextService;
    }

    public ResponseEntity<?> sendEmailCode(SendEmailCodeRequest sendEmailCodeRequest) {
        User user;

        try {
            user = userRepository.findByUsername(sendEmailCodeRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        if (user.isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.OK).body(Collections
                    .singletonMap("message", "Your email address has already been verified. No further action is required."));
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<ConfirmationCode> userConfirmationCodes = confirmationCodeRepository.findByUser(user, sort)
                .orElse(new ArrayList<>());

        if (userConfirmationCodes.size() >= 3) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Collections
                    .singletonMap("message", "You have reached your daily limit for confirmation code requests. Please try again tomorrow or contact our support team for further assistance."));
        }

        if (!userConfirmationCodes.isEmpty() && userConfirmationCodes.getLast().getExpiresAt().getTime() > System.currentTimeMillis()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Collections
                    .singletonMap("message", "You've recently requested a code. Please wait 15 minutes from your last request before trying again. If you continue to experience issues, feel free to contact our support team for assistance."));
        }

        ConfirmationCode confirmationCode = new ConfirmationCode(user);

        try {
            sendConfirmationEmail(user.getUsername(), user.getFirstname(), confirmationCode.getCode());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", "Failed to send confirmation email."));
        }

        confirmationCodeRepository.save(confirmationCode);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "A confirmation email has been sent."));
    }

    public ResponseEntity<?> confirmEmail(String code, HttpServletRequest request) {
        ConfirmationCode confirmationCode;

        try {
            confirmationCode = confirmationCodeRepository.findByCode(code)
                    .orElseThrow(() -> new CodeInvalidException("Invalid code."));

            confirmationCode.validateCode();
        } catch (CodeInvalidException | CodeExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        User user = confirmationCode.getUser();

        user.setEmailVerified(true);

        confirmationCode.setExpired(true);

        User savedUser = userRepository.save(user);

        SecurityUser securityUser = userMapper.userToSecurityUser(savedUser);

        securityContextService.authenticateUser(securityUser, request);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Your email has been successfully confirmed. You can now access all features of your account."));
    }

    @Async
    public void sendConfirmationEmail(String to, String firstname, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("code", code);
        context.setVariable("supportEmail", supportEmail);
        context.setVariable("websiteUrl", websiteUrl);

        String emailContent = templateEngine.process("confirmation-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(fromEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Confirm Your Registration with Spring Bite");
        message.setContent(emailContent, "text/html");

        mailSender.send(message);
    }

    @Async
    public void sendResetPasswordEmail(String to, String firstname, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("firstname", firstname);
        context.setVariable("code", code);
        context.setVariable("supportEmail", supportEmail);
        context.setVariable("websiteUrl", websiteUrl);

        String emailContent = templateEngine.process("password-reset-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(fromEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("Reset Your Password");
        message.setContent(emailContent, "text/html");

        mailSender.send(message);
    }
}
