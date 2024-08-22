package com.springbite.authorization_server.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import com.springbite.authorization_server.config.TrustedIssuer;
import com.springbite.authorization_server.exceptions.InvalidJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final String issuer = "http://10.66.66.9";
    private final TrustedIssuer trustedIssuer;

    public JwtService(TrustedIssuer trustedIssuer) {
        this.trustedIssuer = trustedIssuer;
    }

    private Map<String, Object> setCommonClaims(
            String sub,
            Long uid,
            String aud,
            Collection<GrantedAuthority> authorities
    ) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("sub", sub);

        claims.put("uid", uid);

        claims.put("aud", aud);

        claims.put("jti", UUID.randomUUID().toString());

        claims.put("authorities", authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
        );

        return claims;
    }

    private Map<String, Object> setAccessClaims(
            String sub,
            Long uid,
            String aud,
            Set<String> scopes,
            Collection<GrantedAuthority> authorities
    ) {
        Map<String, Object> accessClaims = setCommonClaims(sub, uid, aud, authorities);

        if (scopes != null && !scopes.isEmpty()) {
            accessClaims.put("scope", scopes);
        }

        return accessClaims;
    }

    private Map<String, Object> setIdClaims(
            String sub,
            Long uid,
            String clientId,
            long authTime,
            Collection<GrantedAuthority> authorities,
            HttpServletRequest request
    ) {
        Map<String, Object> idClaims = setCommonClaims(sub, uid, clientId, authorities);

        idClaims.put("azp", clientId);

        idClaims.put("auth_time", authTime);

        idClaims.put("sid", request.getSession().getId());

        return idClaims;
    }

    public String generateAccessToken(
            RSAKey rsaKey,
            String sub,
            Long uid,
            String aud,
            Set<String> scopes,
            Collection<GrantedAuthority> authorities
    ) throws JOSEException {
        Map<String, Object> accessClaims = setAccessClaims(
                sub,
                uid,
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
                .setExpiration(new Date(date.getTime() + Duration.ofHours(1).toMillis()))
                .signWith(rsaKey.toPrivateKey())
                .compact();
    }

    public String generateIdToken(
            RSAKey rsaKey,
            String sub,
            Long uid,
            String clientId,
            long authTime,
            Collection<GrantedAuthority> authorities,
            HttpServletRequest request
    ) throws JOSEException {
        Map<String, Object> idClaims = setIdClaims(
                sub,
                uid,
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
                .setExpiration(new Date(date.getTime() + Duration.ofMinutes(30).toMillis()))
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
