package com.springbite.authorization_server.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.springbite.authorization_server.exceptions.UserNotFoundException;
import com.springbite.authorization_server.mappers.UserMapper;
import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.*;
import com.springbite.authorization_server.repositories.UserRepository;
import com.springbite.authorization_server.security.PasswordGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RegisteredClientRepository registeredClientRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final PasswordGenerator passwordGenerator;

    private final JwkService jwkService;

    private final JwtService jwtService;

    private final RSAKey rsaKey;


    public UserService(
            UserRepository userRepository,
            RegisteredClientRepository registeredClientRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            PasswordGenerator passwordGenerator,
            JwkService jwkService,
            JwtService jwtService,
            RSAKey rsaKey
    ) {
        this.userRepository = userRepository;
        this.registeredClientRepository = registeredClientRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordGenerator = passwordGenerator;
        this.jwkService = jwkService;
        this.jwtService = jwtService;
        this.rsaKey = rsaKey;
    }

    private boolean isUsernameAlreadyExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isPhoneNumberAlreadyExist(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    public boolean isUserAlreadyExist(String username, String phoneNumber) {
        return isUsernameAlreadyExist(username) || isPhoneNumberAlreadyExist(phoneNumber);
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
        String authHeader = request.getHeader("Authorization");

        String base64Credentials = authHeader.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedCredentials = new String(decodedBytes);

        String[] credentials = decodedCredentials.split(":", 2);
        String clientId = credentials[0];

        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);

        if (isUserAlreadyExist(dto.getUsername(), dto.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections
                    .singletonMap("error", "username already exists"));
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        dto.setPassword(encodedPassword);

        User user = userMapper.userDtoToUser(dto);

        User savedUser = userRepository.save(user);

        try {
            Map<String, Object> body = generateResponseBody(
                    userMapper.userToSecurityUser(savedUser),
                    clientId,
                    registeredClient.getScopes(),
                    request
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
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
        String clientId;
        Set<String> scopes;

        if (provider.equals("google")) {
            try {
                RSAPublicKey publicKey = jwkService.google(token);

                username = (String) jwtService.extractClaim(token, publicKey,
                        "email");

                firstname = (String) jwtService.extractClaim(token, publicKey,
                        "given_name");

                lastname = (String) jwtService.extractClaim(token, publicKey,
                        "family_name");

                clientId = (String) jwtService.extractClaim(token, publicKey,
                        "aud");

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                        .singletonMap("error", e.getMessage()));
            }

            if (isUserAlreadyExist(username, signupWithProviderRequest.getPhoneNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections
                        .singletonMap("error", "User already exist."));
            }

            RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);

            if (registeredClient == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                        .singletonMap("error", "Client id not exist."));
            }

            scopes = registeredClient.getScopes();

            String password = passwordGenerator.generateRandomPassword(16);

            String encodedPassword = passwordEncoder.encode(password);

            UserDto dto = new UserDto(
                    username,
                    encodedPassword,
                    firstname,
                    lastname,
                    signupWithProviderRequest.getPhoneNumber()
            );

            user = userMapper.userDtoToUser(dto);

            savedUser = userRepository.save(user);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Invalid provider."));
        }

        try {
            Map<String, Object> body = generateResponseBody(
                    userMapper.userToSecurityUser(savedUser),
                    clientId,
                    scopes,
                    request
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> auth(
            String provider,
            String scope,
            HttpServletRequest request
    ) {
        String token = extractToken(request);

        User user;
        String username;
        String clientId;
        Set<String> scopes = new HashSet<>();

        if (scope != null) {
            scopes = Arrays.stream(scope.split(" "))
                    .collect(Collectors.toSet());
        }

        if (provider.equals("google")) {
            try {
                RSAPublicKey publicKey = jwkService.google(token);

                username = (String) jwtService.extractClaim(token, publicKey,
                        "email");

                clientId = (String) jwtService.extractClaim(token, publicKey,
                        "aud");

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

        try {
            Map<String, Object> body = generateResponseBody(
                    userMapper.userToSecurityUser(user),
                    clientId,
                    scopes,
                    request
            );

            return ResponseEntity.status(HttpStatus.OK).body(body);
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        if (!isUsernameAlreadyExist(forgotPasswordRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", "User with this email does not exist."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Password reset instruction have been sent to your email."));
    }

    public ResponseEntity<?> getUser(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

            UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

            return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
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

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Password have been changed successfully."));
    }

    public ResponseEntity<?> updateUserDetails(Long userId, UpdateUserRequest updateUserRequest) {
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

        Map<String, Object> body = new HashMap<>();

        body.put("message", "User details have been updated successfully.");

        body.put("user", savedUser);

        return ResponseEntity.status(HttpStatus.OK).body(body);
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

    private Map<String, Object> generateResponseBody(
            SecurityUser securityUser,
            String clientId,
            Set<String> scopes,
            HttpServletRequest request) throws JOSEException {
        authenticateUser(securityUser, request);

        long authTime = Instant.now().getEpochSecond();

        String accessToken = jwtService.generateAccessToken(
                rsaKey,
                securityUser.getUsername(),
                clientId,
                scopes,
                (Collection<GrantedAuthority>) securityUser.getAuthorities()
        );

        String idToken = jwtService.generateIdToken(
                rsaKey,
                securityUser.getUsername(),
                clientId,
                authTime,
                securityUser.getUser().getId(),
                (Collection<GrantedAuthority>) securityUser.getAuthorities(),
                request
        );

        Map<String, Object> body = new HashMap<>();

        body.put("access_token", accessToken);

        body.put("scope", scopes);

        int exp = (Integer) jwtService.extractClaim(accessToken, rsaKey.toRSAPublicKey(), "exp");

        long expiresIn = exp - Instant.now().getEpochSecond();

        body.put("expires_in", expiresIn);

        body.put("token_type", "Bearer");

        body.put("id_token", idToken);

        return body;
    }

}
