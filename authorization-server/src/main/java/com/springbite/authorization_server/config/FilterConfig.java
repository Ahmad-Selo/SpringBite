package com.springbite.authorization_server.config;

import com.springbite.authorization_server.filters.ClientAuthFilter;
import com.springbite.authorization_server.filters.JwtAuthFilter;
import com.springbite.authorization_server.filters.PasswordResetTokenFilter;
import com.springbite.authorization_server.repositories.JwkRepository;
import com.springbite.authorization_server.repositories.PasswordResetTokenRepository;
import com.springbite.authorization_server.services.JwkService;
import com.springbite.authorization_server.services.JwtService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class FilterConfig {

    private final PasswordEncoder passwordEncoder;
    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JwkRepository jwkRepository;

    public FilterConfig(
            PasswordEncoder passwordEncoder,
            RegisteredClientRepository registeredClientRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            JwkRepository jwkRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.registeredClientRepository = registeredClientRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.jwkRepository = jwkRepository;
    }

    @Bean
    @ConfigurationProperties(prefix = "trusted")
    public TrustedIssuer trustedIssuer() {
        return new TrustedIssuer();
    }

    @Bean
    public JwtService jwtService(TrustedIssuer trustedIssuer) {
        return new JwtService(trustedIssuer);
    }

    @Bean
    public JwkService jwkService() {
        return new JwkService(jwkRepository);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtService(trustedIssuer()), jwkService());
    }

    @Bean
    public ClientAuthFilter clientAuthFilter() {
        return new ClientAuthFilter(registeredClientRepository, passwordEncoder);
    }

    @Bean
    public PasswordResetTokenFilter passwordRestTokenFilter() {
        return new PasswordResetTokenFilter(passwordResetTokenRepository);
    }

    @Bean
    public FilterRegistrationBean<PasswordResetTokenFilter> passwordRestTokenFilterRegistrationBean(PasswordResetTokenFilter filter) {
        FilterRegistrationBean<PasswordResetTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/reset-password");

        return registrationBean;
    }

    @Bean
    @Order(3)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(
                jwtAuthFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                clientAuthFilter(),
                JwtAuthFilter.class
        );

        return http.build();
    }
}
