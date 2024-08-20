package com.springbite.authorization_server.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.springbite.authorization_server.exceptions.InvalidJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

    private Map<String, Object> setAccessClaims(
            String sub,
            String aud,
            Set<String> scopes,
            Collection<GrantedAuthority> authorities
    ) {
        Map<String, Object> accessClaims = new HashMap<>();

        accessClaims.put("sub", sub);

        accessClaims.put("aud", aud);

        if (scopes != null && !scopes.isEmpty()) {
            accessClaims.put("scope", scopes);
        }

        accessClaims.put("jti", UUID.randomUUID().toString());

        accessClaims.put("authorities", authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return accessClaims;
    }

    private Map<String, Object> setIdClaims(
            String sub,
            String clientId,
            long authTime,
            Collection<GrantedAuthority> authorities,
            HttpServletRequest request
    ) {
        Map<String, Object> idClaims = new HashMap<>();

        idClaims.put("sub", sub);

        idClaims.put("aud", clientId);

        idClaims.put("azp", clientId);

        idClaims.put("auth_time", authTime);

        idClaims.put("jti", UUID.randomUUID().toString());

        idClaims.put("authorities", authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        idClaims.put("sid", request.getSession().getId());

        return idClaims;
    }

    public String generateAccessToken(
            RSAKey rsaKey,
            String sub,
            String aud,
            Set<String> scopes,
            Collection<GrantedAuthority> authorities
    ) throws JOSEException {
        Map<String, Object> accessClaims = setAccessClaims(
                sub,
                aud,
                scopes,
                authorities
        );

        return generateAccessToken(rsaKey, accessClaims);
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

    public String generateIdToken(
            RSAKey rsaKey,
            String sub,
            String clientId,
            long authTime,
            Collection<GrantedAuthority> authorities,
            HttpServletRequest request
    ) throws JOSEException {
        Map<String, Object> idClaims = setIdClaims(
                sub,
                clientId,
                authTime,
                authorities,
                request
        );

        return generateIdToken(rsaKey, idClaims);
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
