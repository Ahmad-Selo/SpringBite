package com.springbite.authorization_server.exception;

public class CodeInvalidException extends RuntimeException {

    public CodeInvalidException(String message) {
        super(message);
    }

    public CodeInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
