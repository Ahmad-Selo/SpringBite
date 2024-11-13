package com.springbite.authorization_server.exception;

public class CodeExpiredException extends RuntimeException {
    public CodeExpiredException(String message) {
        super(message);
    }

    public CodeExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
