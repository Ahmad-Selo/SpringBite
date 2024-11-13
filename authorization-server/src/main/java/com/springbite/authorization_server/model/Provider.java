package com.springbite.authorization_server.model;

import org.springframework.security.oauth2.jwt.JwtDecoder;

public interface Provider {

    String PATTERN = "^/auth/(google)$";

    String GOOGLE = "google";

    JwtDecoder jwtDecoder();

}
