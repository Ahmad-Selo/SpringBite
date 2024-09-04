package com.springbite.authorization_server.services;

import com.springbite.authorization_server.entities.ConfirmationCode;
import com.springbite.authorization_server.exceptions.CodeExpiredException;
import com.springbite.authorization_server.exceptions.CodeInvalidException;
import com.springbite.authorization_server.exceptions.UserNotFoundException;
import com.springbite.authorization_server.mappers.UserMapper;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.*;
import com.springbite.authorization_server.repositories.ConfirmationCodeRepository;
import com.springbite.authorization_server.repositories.UserRepository;
import com.springbite.authorization_server.security.Role;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ConfirmationCodeRepository confirmationCodeRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public UserService(
            UserRepository userRepository,
            ConfirmationCodeRepository confirmationCodeRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.confirmationCodeRepository = confirmationCodeRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public ResponseEntity<?> sendEmailCode(Long userId) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("No user with this id exits."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
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
            emailService.sendConfirmationEmail(user.getUsername(), user.getFirstname(), confirmationCode.getCode());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", "Failed to send password confirmation email."));
        }

        confirmationCodeRepository.save(confirmationCode);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "A confirmation email has been sent."));
    }

    public ResponseEntity<?> confirmEmail(Long userId, String code) {
        ConfirmationCode confirmationCode;

        try {
            confirmationCode = confirmationCodeRepository.findByCode(code)
                    .orElseThrow(() -> new CodeInvalidException("Invalid code."));

            confirmationCode.validateCode(userId);
        } catch (CodeInvalidException | CodeExpiredException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        User user = confirmationCode.getUser();

        user.setEmailVerified(true);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Your email has been successfully confirmed. You can now access all features of your account."));
    }

    public ResponseEntity<?> getUser(Long userId) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    public ResponseEntity<?> searchUser(String username, String phoneNumber) {
        List<UserPromoteResponse> users;

        if (phoneNumber != null) {
            users = userRepository.findByPhoneNumberContaining(phoneNumber)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(userMapper::userToUserPromoteResponse)
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findByUsernameContaining(username)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(userMapper::userToUserPromoteResponse)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);

    }

    public ResponseEntity<?> promoteUser(Long userId, PromoteRequest promoteRequest) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        Role role = Role.valueOf(promoteRequest.getRole());

        user.setRole(role);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "The user has been successfully promoted to the new role."));
    }

    public ResponseEntity<?> changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Password incorrect."));
        }

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("message", "Your new password cannot be the same as your current password. Please choose a different password to ensure the security of your account."));
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Password have been changed successfully."));
    }

    public ResponseEntity<?> updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        String username = updateUserRequest.getUsername();
        String firstname = updateUserRequest.getFirstname();
        String lastname = updateUserRequest.getLastname();
        String phoneNumber = updateUserRequest.getPhoneNumber();

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        if (firstname != null && !firstname.isBlank()) {
            user.setFirstname(firstname);
        }

        if (lastname != null && !lastname.isBlank()) {
            user.setLastname(lastname);
        }

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            user.setPhoneNumber(phoneNumber);
        }

        User savedUser = userRepository.save(user);

        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(savedUser);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    public ResponseEntity<?> deleteUser(Long userId, DeleteUserRequest deleteUserRequest) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        if (!passwordEncoder.matches(deleteUserRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Password incorrect."));
        }

        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "User have been deleted successfully."));
    }

}
