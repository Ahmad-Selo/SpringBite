package com.springbite.authorization_server.exceptions;

public class TokenInvalidException extends Exception {
    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
