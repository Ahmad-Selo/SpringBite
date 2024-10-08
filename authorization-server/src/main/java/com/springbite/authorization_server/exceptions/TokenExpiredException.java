package com.springbite.authorization_server.exceptions;

public class TokenExpiredException extends Exception {
    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
