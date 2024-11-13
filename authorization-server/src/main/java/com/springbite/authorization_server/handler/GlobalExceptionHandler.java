package com.springbite.authorization_server.handler;

import com.springbite.authorization_server.exception.*;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(
            AccessDeniedException e
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                .singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", "Missing body"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        var errors = new HashMap<>();

        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", errors));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(
            UserNotFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> handleMessagingException(
            MessagingException e
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", "Failed to send email."));
    }

    @ExceptionHandler(CodeInvalidException.class)
    public ResponseEntity<?> handleCodeInvalidException(
            CodeInvalidException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections
                .singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(CodeExpiredException.class)
    public ResponseEntity<?> handleCodeExpiredException(
            CodeExpiredException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections
                .singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<?> handleTokenInvalidException(
            TokenInvalidException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections
                .singletonMap("error", e.getMessage()));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(
            TokenExpiredException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap(
                "error", e.getMessage())
        );
    }
}
