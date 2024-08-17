package com.springbite.authorization_server.exceptions;

public class InvalidProvider extends Exception {

    public InvalidProvider(String message) {
        super(message);
    }

    public InvalidProvider(String message, Throwable cause) {
        super(message, cause);
    }
}
