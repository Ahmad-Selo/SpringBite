package com.springbite.authorization_server.exceptions;

public class CodeExpiredException extends Exception {
    public CodeExpiredException(String message) {
        super(message);
    }

    public CodeExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
