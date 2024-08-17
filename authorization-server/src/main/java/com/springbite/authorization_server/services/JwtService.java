package com.springbite.authorization_server.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.springbite.authorization_server.exceptions.InvalidJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Service
public class JwtService {


    private final String issuer = "http://10.66.66.9";
    private final Set<String> issuers;
    private final Set<String> clients;

    public JwtService(Set<String> issuers, Set<String> clients) {
        this.issuers = issuers;
        this.clients = clients;
    }

    public Set<String> getIssuers() {
        return issuers;
    }

    public Set<String> getClients() {
        return clients;
    }

    public String generateIdToken(RSAKey rsaKey, Map<String, Object> claims)
            throws JOSEException {
        Date date = new Date();

        return Jwts.builder()
                .setHeaderParam("kid", rsaKey.getKeyID())
                .setHeaderParam("alg", rsaKey.toPrivateKey().getAlgorithm())
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + 1800000))
                .signWith(rsaKey.toPrivateKey())
                .compact();
    }

    public String generateAccessToken(RSAKey rsaKey, Map<String, Object> claims)
            throws JOSEException {
        Date date = new Date();

        return Jwts.builder()
                .setHeaderParam("kid", rsaKey.getKeyID())
                .setHeaderParam("alg", rsaKey.toPrivateKey().getAlgorithm())
                .setClaims(claims)
                .setNotBefore(date)
                .setIssuer(issuer)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + 300000))
                .signWith(rsaKey.toPrivateKey())
                .compact();
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
        return issuers.contains((String) extractClaim(token, publicKey, "iss"));
    }

    private boolean validateAud(String token, RSAPublicKey publicKey) {
        return clients.contains((String) extractClaim(token, publicKey, "aud"));
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
