package com.springbite.authorization_server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/csrf")
public class CsrfController {

    @GetMapping
    public ResponseEntity<?> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("csrf_token", csrfToken.getToken()));
    }
}
