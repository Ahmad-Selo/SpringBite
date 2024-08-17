package com.springbite.authorization_server.exceptions;

public class MissingBearerToken extends Exception {

    public MissingBearerToken(String message) {
        super(message);
    }

    public MissingBearerToken(String message, Throwable cause) {
        super(message, cause);
    }
}
