package com.springbite.authorization_server.exceptions;

import org.springframework.security.core.AuthenticationException;

public class EmailNotVerifiedException extends AuthenticationException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }

    public EmailNotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
