package com.springbite.authorization_server.services;

import com.springbite.authorization_server.config.TrustedIssuer;
import com.springbite.authorization_server.exceptions.InvalidJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

@Service
public class JwtService {

    private final TrustedIssuer trustedIssuer;

    public JwtService(TrustedIssuer trustedIssuer) {
        this.trustedIssuer = trustedIssuer;
    }

    public void validateToken(String token, RSAPublicKey publicKey) throws InvalidJwt {
        if (validateIss(token, publicKey) &&
                validateAud(token, publicKey) &&
                validateExp(token, publicKey)) {
            return;
        }
        throw new InvalidJwt("Invalid jwt");
    }

    private boolean validateIss(String token, RSAPublicKey publicKey) {
        return trustedIssuer.getIssuers().contains((String) extractClaim(token, publicKey, "iss"));
    }

    private boolean validateAud(String token, RSAPublicKey publicKey) {
        return trustedIssuer.getClients().contains((String) extractClaim(token, publicKey, "aud"));
    }

    private boolean validateExp(String token, RSAPublicKey publicKey) {
        int exp = (Integer) extractClaim(token, publicKey, "exp");
        long currentTime = Instant.now().getEpochSecond();
        return currentTime < exp;
    }

    public Claims extractClaims(String token, RSAPublicKey publicKey) {
        if (publicKey == null) {
            throw new NullPointerException("public key is null");
        }

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Object extractClaim(String token, RSAPublicKey publicKey, String key) {
        return extractClaims(token, publicKey).get(key);
    }
}
