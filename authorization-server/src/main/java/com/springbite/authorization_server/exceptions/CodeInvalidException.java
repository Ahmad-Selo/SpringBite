package com.springbite.authorization_server.exceptions;

public class CodeInvalidException extends Exception {

    public CodeInvalidException(String message) {
        super(message);
    }

    public CodeInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
