package com.springbite.authorization_server.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.springbite.authorization_server.mappers.UserMapper;
import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.UserDto;
import com.springbite.authorization_server.repositories.UserRepository;
import com.springbite.authorization_server.security.PasswordGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RegisteredClientRepository registeredClientRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final PasswordGenerator passwordGenerator;

    private final JwkSetService jwkSetService;

    private final JwtService jwtService;

    private final RSAKey rsaKey;


    public UserService(
            UserRepository userRepository,
            RegisteredClientRepository registeredClientRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            PasswordGenerator passwordGenerator,
            JwkSetService jwkSetService,
            JwtService jwtService,
            RSAKey rsaKey
    ) {
        this.userRepository = userRepository;
        this.registeredClientRepository = registeredClientRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordGenerator = passwordGenerator;
        this.jwkSetService = jwkSetService;
        this.jwtService = jwtService;
        this.rsaKey = rsaKey;
    }

    private boolean isUsernameAlreadyExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isPhoneNumberAlreadyExists(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }

    public boolean isUserAlreadyExists(String username, String phoneNumber) {
        return isUsernameAlreadyExists(username) || isPhoneNumberAlreadyExists(phoneNumber);
    }

    private void authenticateUser(SecurityUser securityUser) {
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(
                securityUser.getUsername(),
                securityUser.getPassword(),
                securityUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
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

        if (isUserAlreadyExists(dto.getUsername(), dto.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections
                    .singletonMap("error", "username already exists"));
        }

        User user = userMapper.toUser(dto);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        Map<String, Object> body;
        try {
            body = generateResponseBody(
                    userMapper.userToSecurityUser(user),
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

    public ResponseEntity<?> auth(
            UserDto dto,
            String provider,
            String clientId,
            String scope,
            HttpServletRequest request
    ) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", "Bearer token is missing."));
        }

        String token = authHeader.substring(7);

        if (dto.getPhoneNumber() == null || dto.getPhoneNumber().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", "Phone number must not be blank."));
        }

        User user;
        String username;
        String firstname;
        String lastname;
        Set<String> scopes = new HashSet<>();
        HttpStatus httpStatus;

        if (scope != null) {
            scopes = Arrays.stream(scope.split(" "))
                    .collect(Collectors.toSet());
        }

        if (provider.equals("google")) {
            try {
                jwkSetService.google(token);

                jwtService.validateToken(token, jwkSetService.getPublicKey());

                username = (String) jwtService.extractClaim(token, jwkSetService.getPublicKey(),
                        "email");

                firstname = (String) jwtService.extractClaim(token, jwkSetService.getPublicKey(),
                        "given_name");

                lastname = (String) jwtService.extractClaim(token, jwkSetService.getPublicKey(),
                        "family_name");

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                        .singletonMap("error", e.getMessage()));
            }

            if (isUserAlreadyExists(dto.getUsername(), dto.getPhoneNumber())) {
                user = userRepository.findByUsername(username).orElse(null);
                httpStatus = HttpStatus.OK;
            } else {
                user = userMapper.toUser(dto);
                user.setUsername(username);
                user.setFirstname(firstname);
                user.setLastname(lastname);
                if (user.getPassword() == null) {
                    String password = passwordGenerator.generateRandomPassword(16);
                    String encodedPassword = passwordEncoder.encode(password);
                    user.setPassword(encodedPassword);
                }
                userRepository.save(user);
                httpStatus = HttpStatus.CREATED;
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Invalid provider."));
        }

        Map<String, Object> body;
        try {
            body = generateResponseBody(
                    userMapper.userToSecurityUser(user),
                    clientId,
                    scopes,
                    request
            );

            return ResponseEntity.status(httpStatus).body(body);
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    private Map<String, Object> generateResponseBody(
            SecurityUser user,
            String clientId,
            Set<String> scopes,
            HttpServletRequest request) throws JOSEException {
        authenticateUser(user);

        long auth_time = Instant.now().getEpochSecond();

        Map<String, Object> accessClaims = setAccessClaims(
                user.getUsername(),
                clientId,
                scopes,
                (Collection<GrantedAuthority>) user.getAuthorities()
        );

        String accessToken = jwtService.generateAccessToken(rsaKey, accessClaims);

        Map<String, Object> idClaims = setIdClaims(
                user.getUsername(),
                clientId,
                auth_time,
                (Collection<GrantedAuthority>) user.getAuthorities(), request
        );

        String idToken = jwtService.generateIdToken(rsaKey, idClaims);

        Map<String, Object> body = new HashMap<>();

        body.put(
                "access_token", accessToken
        );

        body.put("scope", scopes);

        body.put("id_token", idToken);

        body.put("token_type", "Bearer");

        int exp = (Integer) jwtService.extractClaim(accessToken, rsaKey.toRSAPublicKey(), "exp");

        long expiresIn = exp - Instant.now().getEpochSecond();

        body.put("expires_in", expiresIn);

        return body;
    }

    private Map<String, Object> setAccessClaims(
            String sub,
            String aud,
            Set<String> scopes,
            Collection<GrantedAuthority> authorities) {
        Map<String, Object> accessClaims = new HashMap<>();

        accessClaims.put("sub", sub);

        accessClaims.put("aud", aud);

        if (!scopes.isEmpty()) {
            accessClaims.put("scope", scopes);
        }

        accessClaims.put("jti", UUID.randomUUID().toString());

        accessClaims.put("authorities", authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return accessClaims;
    }

    private Map<String, Object> setIdClaims(
            String sub,
            String clientId,
            long auth_time,
            Collection<GrantedAuthority> authorities,
            HttpServletRequest request) {
        Map<String, Object> idClaims = new HashMap<>();

        idClaims.put("sub", sub);
        idClaims.put("aud", clientId);
        idClaims.put("azp", clientId);
        idClaims.put("auth_time", auth_time);
        idClaims.put("jti", UUID.randomUUID().toString());
        idClaims.put("authorities", authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        idClaims.put("sid", request.getSession().getId());

        return idClaims;
    }
}
