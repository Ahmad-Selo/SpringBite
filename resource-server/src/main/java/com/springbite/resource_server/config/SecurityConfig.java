package com.springbite.resource_server.config;

import com.springbite.resource_server.config.security.JwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${keySetUri}")
    private String keySetUri;

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfig(JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(
                o -> o.jwt(
                        j -> j.jwkSetUri(keySetUri)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                )
        );

        http.authorizeHttpRequests(
                c -> c.anyRequest().authenticated()
        );

        return  http.build();
    }
}
