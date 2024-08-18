package com.springbite.resource_server.exceptions;

public class ValueOutOfRangeException extends Exception {

    public ValueOutOfRangeException(String message) {
        super(message);
    }

    public ValueOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
