package com.springbite.authorization_server.security.oauth2;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Predicate;

public class JwtAudValidator implements OAuth2TokenValidator<Jwt> {

    private final JwtClaimValidator<Object> validator;

    public JwtAudValidator(String aud) {
        Assert.notNull(aud, "aud cannot be null");

        Predicate<Object> testClaimValue = (claimValue) -> {
            if(claimValue == null) {
                return false;
            }

            if(claimValue instanceof List<?>) {
                return ((List<?>) claimValue).contains(aud);
            }
            else {
                return aud.equals(claimValue.toString());
            }
        };
        this.validator = new JwtClaimValidator<>(JwtClaimNames.AUD, testClaimValue);
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        Assert.notNull(token, "token cannot be null");
        return this.validator.validate(token);
    }
}
