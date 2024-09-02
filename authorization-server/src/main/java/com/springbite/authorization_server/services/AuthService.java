package com.springbite.authorization_server.services;

import com.springbite.authorization_server.entities.ConfirmationCode;
import com.springbite.authorization_server.mappers.UserMapper;
import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.SignupWithProviderRequest;
import com.springbite.authorization_server.models.dtos.UserDto;
import com.springbite.authorization_server.models.dtos.UserResponseDto;
import com.springbite.authorization_server.repositories.ConfirmationCodeRepository;
import com.springbite.authorization_server.repositories.UserRepository;
import com.springbite.authorization_server.security.PasswordGenerator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class AuthService {


    private final UserRepository userRepository;

    private final ConfirmationCodeRepository confirmationCodeRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final PasswordGenerator passwordGenerator;

    private final JwkService jwkService;

    private final JwtService jwtService;

    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            ConfirmationCodeRepository confirmationCodeRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            PasswordGenerator passwordGenerator,
            JwkService jwkService,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.confirmationCodeRepository = confirmationCodeRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordGenerator = passwordGenerator;
        this.jwkService = jwkService;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public boolean isUsernameAlreadyExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isPhoneNumberAlreadyExist(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        return authHeader.substring(7);
    }

    private void authenticateUser(SecurityUser securityUser, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                securityUser,
                null,
                securityUser.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetails(request));

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    public ResponseEntity<?> signup(
            UserDto dto,
            HttpServletRequest request
    ) {
        if (isUsernameAlreadyExist(dto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections
                    .singletonMap("error", "username already exists."));
        }

        if (isPhoneNumberAlreadyExist(dto.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections
                    .singletonMap("error", "phone number already exists."));
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        dto.setPassword(encodedPassword);

        User user = userMapper.userDtoToUser(dto, false);

        User savedUser = userRepository.save(user);

        ConfirmationCode confirmationCode = new ConfirmationCode(savedUser);

        confirmationCodeRepository.save(confirmationCode);

        try {
            emailService.sendConfirmationEmail(
                    savedUser.getUsername(),
                    savedUser.getFirstname(),
                    confirmationCode.getCode()
            );
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", "Failed to send confirmation email."));
        }

        SecurityUser securityUser = userMapper.userToSecurityUser(savedUser);

        authenticateUser(securityUser, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections
                .singletonMap("message", "Signup successful. A confirmation email has been sent.")
        );
    }

    public ResponseEntity<?> signupWithProvider(
            SignupWithProviderRequest signupWithProviderRequest,
            String provider,
            HttpServletRequest request
    ) {
        String token = extractToken(request);

        User user;
        User savedUser;
        String username;
        String firstname;
        String lastname;
        String picture;
        boolean emailVerified;

        if (provider.equals("google")) {
            try {
                RSAPublicKey publicKey = jwkService.google(token);

                username = (String) jwtService.extractClaim(token, publicKey,
                        "email");

                firstname = (String) jwtService.extractClaim(token, publicKey,
                        "given_name");

                lastname = (String) jwtService.extractClaim(token, publicKey,
                        "family_name");

                emailVerified = (Boolean) jwtService.extractClaim(token, publicKey,
                        "email_verified");

                picture = (String) jwtService.extractClaim(token, publicKey,
                        "picture");

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                        .singletonMap("error", e.getMessage()));
            }

            if (isUsernameAlreadyExist(username)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections
                        .singletonMap("error", "username already exist."));
            }

            if (isPhoneNumberAlreadyExist(signupWithProviderRequest.getPhoneNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections
                        .singletonMap("error", "phone number already exist."));
            }

            String password = passwordGenerator.generateRandomPassword(16);

            String encodedPassword = passwordEncoder.encode(password);

            UserDto dto = new UserDto(
                    username,
                    encodedPassword,
                    firstname,
                    lastname,
                    signupWithProviderRequest.getPhoneNumber(),
                    picture
            );

            user = userMapper.userDtoToUser(dto, emailVerified);

            savedUser = userRepository.save(user);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Invalid provider."));
        }

        SecurityUser securityUser = userMapper.userToSecurityUser(savedUser);

        authenticateUser(securityUser, request);

        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    public ResponseEntity<?> auth(
            String provider,
            HttpServletRequest request
    ) {
        String token = extractToken(request);

        User user;
        String username;

        if (provider.equals("google")) {
            try {
                RSAPublicKey publicKey = jwkService.google(token);

                username = (String) jwtService.extractClaim(token, publicKey,
                        "email");

                user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));

            } catch (UsernameNotFoundException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                        .singletonMap("error", e.getMessage()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                        .singletonMap("error", e.getMessage()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Invalid provider."));
        }

        SecurityUser securityUser = userMapper.userToSecurityUser(user);

        authenticateUser(securityUser, request);

        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }
}
