package com.springbite.authorization_server.exceptions;

public class InvalidJwt extends Exception {

    public InvalidJwt(String message) {
        super(message);
    }

    public InvalidJwt(String message, Throwable cause) {
        super(message, cause);
    }
}
