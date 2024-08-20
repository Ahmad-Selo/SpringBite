package com.springbite.authorization_server.filters;

import com.springbite.authorization_server.exceptions.InvalidJwt;
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
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter().print(Collections
                    .singletonMap("error", "Bearer token is missing"));
            return;
        }

        String token = authHeader.substring(7);

        if (provider.equals("google")) {
            RSAPublicKey publicKey = jwkService.google(token);

            try {
                jwtService.validateToken(token, publicKey);

                chain.doFilter(httpRequest, httpResponse);
            } catch (InvalidJwt e) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().print(Collections
                        .singletonMap("error", e.getMessage()));
            }
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter().print(Collections
                    .singletonMap("error", "Invalid provider."));
        }
    }
}
