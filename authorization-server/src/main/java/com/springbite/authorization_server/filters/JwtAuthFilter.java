package com.springbite.authorization_server.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbite.authorization_server.services.JwkService;
import com.springbite.authorization_server.services.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

public class JwtAuthFilter implements Filter {

    private final JwtService jwtService;

    private final JwkService jwkService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthFilter(JwtService jwtService, JwkService jwkService) {
        this.jwtService = jwtService;
        this.jwkService = jwkService;
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

        if (!uri.startsWith("/signup/") && !uri.startsWith("/auth/")) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        String provider;

        if (uri.startsWith("/signup/")) {
            provider = uri.substring(8);
        } else {
            provider = uri.substring(6);
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");

            String body = objectMapper.writeValueAsString(Collections
                    .singletonMap("error", "Bearer token is missing"));
            httpResponse.getWriter().write(body);
            return;
        }

        String token = authHeader.substring(7);

        if (provider.equals("google")) {
            try {
                RSAPublicKey publicKey = jwkService.google(token);

                jwtService.validateToken(token, publicKey);

                chain.doFilter(httpRequest, httpResponse);
            } catch (Exception e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");

                String body = objectMapper.writeValueAsString(Collections
                        .singletonMap("error", e.getMessage()));
                httpResponse.getWriter().write(body);
            }
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json");

            String body = objectMapper.writeValueAsString(Collections
                    .singletonMap("error", "Invalid provider."));
            httpResponse.getWriter().write(body);
        }
    }
}
