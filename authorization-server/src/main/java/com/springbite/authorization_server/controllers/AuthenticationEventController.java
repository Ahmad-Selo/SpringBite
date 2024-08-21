package com.springbite.authorization_server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class AuthenticationEventController {

    @GetMapping("/home")
    public ResponseEntity<?> home() {
        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "successfully!"));
    }

    @GetMapping("/error")
    public ResponseEntity<?> error() {
        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("error", "error!"));
    }
}
