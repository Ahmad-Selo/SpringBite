package com.springbite.authorization_server.services;

import com.springbite.authorization_server.entities.PasswordResetCode;
import com.springbite.authorization_server.entities.PasswordResetToken;
import com.springbite.authorization_server.exceptions.CodeExpiredException;
import com.springbite.authorization_server.exceptions.CodeInvalidException;
import com.springbite.authorization_server.exceptions.TokenExpiredException;
import com.springbite.authorization_server.exceptions.TokenInvalidException;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.PasswordForgotRequest;
import com.springbite.authorization_server.models.dtos.PasswordResetRequest;
import com.springbite.authorization_server.repositories.PasswordResetCodeRepository;
import com.springbite.authorization_server.repositories.PasswordResetTokenRepository;
import com.springbite.authorization_server.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class PasswordRecoveryService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordResetCodeRepository passwordResetCodeRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final EmailService emailService;

    public PasswordRecoveryService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetCodeRepository passwordResetCodeRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetCodeRepository = passwordResetCodeRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    public ResponseEntity<?> forgotPassword(PasswordForgotRequest passwordForgotRequest) throws MessagingException {
        String username = passwordForgotRequest.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        List<PasswordResetCode> userPasswordResetCodes = passwordResetCodeRepository.findByUser(user, sort)
                .orElse(new ArrayList<>());

        if (userPasswordResetCodes.size() >= 3) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Collections
                    .singletonMap("message", "You have reached your daily limit for reset code requests. You can try again tomorrow or get in touch with our support team if you need immediate help."));
        }

        if (!userPasswordResetCodes.isEmpty() && userPasswordResetCodes.getLast().getExpiresAt().getTime() > System.currentTimeMillis()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(Collections
                    .singletonMap("message", "You've recently requested a reset code. Please wait 15 minutes from your last request before trying again. If you continue to experience issues, feel free to contact our support team for assistance."));
        }

        PasswordResetCode passwordResetCode = new PasswordResetCode(user);

        emailService.sendResetPasswordEmail(username, user.getFirstname(), passwordResetCode.getCode());

        passwordResetCodeRepository.save(passwordResetCode);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Password reset instruction have been sent to your email."));
    }

    public ResponseEntity<?> verifyCode(String code) throws CodeInvalidException, CodeExpiredException {
        PasswordResetCode passwordResetCode = passwordResetCodeRepository.findByCode(code)
                .orElseThrow(() -> new CodeInvalidException("Invalid code."));

        passwordResetCode.validateCode();

        passwordResetCode.setExpired(true);

        PasswordResetCode savedPasswordResetCode = passwordResetCodeRepository.save(passwordResetCode);

        PasswordResetToken passwordResetToken = new PasswordResetToken(savedPasswordResetCode);

        passwordResetTokenRepository.save(passwordResetToken);

        long expiresIn = passwordResetToken.getExpiresAt().toInstant().getEpochSecond() - Instant.now().getEpochSecond();

        Map<String, Object> body = Map.of(
                "reset_token", passwordResetToken.getToken(),
                "expires_in", expiresIn
        );

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public ResponseEntity<?> resetPassword(
            String token,
            PasswordResetRequest passwordResetRequest
    ) throws TokenInvalidException, TokenExpiredException {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidException("Invalid token."));

        passwordResetToken.validateToken();

        User user = passwordResetToken.getPasswordResetCode().getUser();

        if (passwordEncoder.matches(passwordResetRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("message", "Your new password cannot be the same as your current password. Please choose a different password to ensure the security of your account."));
        }

        String encodedPassword = passwordEncoder.encode(passwordResetRequest.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        passwordResetToken.setExpired(true);

        passwordResetTokenRepository.save(passwordResetToken);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Your password has been reset successfully. You can now login with your new password."));
    }
}
