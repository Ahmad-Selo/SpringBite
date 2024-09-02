package com.springbite.authorization_server.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

public class ClientAuthFilter implements Filter {

    private final RegisteredClientRepository registeredClientRepository;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ClientAuthFilter(RegisteredClientRepository registeredClientRepository, PasswordEncoder passwordEncoder) {
        this.registeredClientRepository = registeredClientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();

        if (!uri.equals("/csrf")) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");

            String body = objectMapper.writeValueAsString(Collections
                    .singletonMap("error", "Authorization header missing or invalid."));
            httpResponse.getWriter().write(body);
            return;
        }

        String base64Credentials = authHeader.substring(6);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        String decodedCredentials = new String(decodedBytes);

        String[] credentials = decodedCredentials.split(":", 2);
        String clientId = credentials[0];
        String clientSecret = credentials[1];

        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);

        if (registeredClient == null || !passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");

            String body = objectMapper.writeValueAsString(Collections
                    .singletonMap("error", "Invalid client credentials."));
            httpResponse.getWriter().write(body);
            return;
        }
        chain.doFilter(httpRequest, httpResponse);
    }
}
