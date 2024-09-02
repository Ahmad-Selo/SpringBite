package com.springbite.authorization_server.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}
