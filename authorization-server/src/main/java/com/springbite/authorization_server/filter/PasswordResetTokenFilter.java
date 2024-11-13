package com.springbite.authorization_server.filter;

import com.springbite.authorization_server.entity.PasswordResetToken;
import com.springbite.authorization_server.exception.TokenExpiredException;
import com.springbite.authorization_server.exception.TokenInvalidException;
import com.springbite.authorization_server.model.dto.JsonResponse;
import com.springbite.authorization_server.repository.PasswordResetTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Setter
@Component
public class PasswordResetTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(PasswordResetTokenFilter.class);

    private static final String PATTERN = "/reset-password";

    private static final String TOKEN_PARAMETER = "token";

    private RequestMatcher requestMatcher = new AntPathRequestMatcher(PATTERN, HttpMethod.POST.name());

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        if(!requestMatcher.matches(request)) {
            chain.doFilter(request, response);
            return;
        }

        String token = request.getParameter(TOKEN_PARAMETER);
        PasswordResetToken passwordResetToken;

        try {
            passwordResetToken = passwordResetTokenRepository.findByToken(token)
                    .orElseThrow(() -> new TokenInvalidException("Invalid token"));

            passwordResetToken.validateToken();

            chain.doFilter(request, response);
        } catch (TokenInvalidException | TokenExpiredException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            response.getWriter().write(JsonResponse.json(Collections.singletonMap(
                    "message", e.getMessage()
            )));
        }
    }
}
