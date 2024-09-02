package com.springbite.authorization_server.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbite.authorization_server.entities.PasswordResetToken;
import com.springbite.authorization_server.exceptions.TokenExpiredException;
import com.springbite.authorization_server.exceptions.TokenInvalidException;
import com.springbite.authorization_server.repositories.PasswordResetTokenRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

public class PasswordResetTokenFilter implements Filter {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PasswordResetTokenFilter(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = httpRequest.getParameter("token");
        PasswordResetToken passwordResetToken;

        try {
            passwordResetToken = passwordResetTokenRepository.findByToken(token)
                    .orElseThrow(() -> new TokenInvalidException("Invalid token."));

            passwordResetToken.validateToken();

            chain.doFilter(request, response);
        } catch (TokenInvalidException | TokenExpiredException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");

            String body = objectMapper.writeValueAsString(Collections
                    .singletonMap("error", e.getMessage()));
            httpResponse.getWriter().write(body);
        }
    }
}
