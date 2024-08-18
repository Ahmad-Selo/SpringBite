package com.springbite.authorization_server.services;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.springbite.authorization_server.models.dtos.JwkSetResponse;
import com.springbite.authorization_server.security.JwkSet;
import io.jsonwebtoken.security.InvalidKeyException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Service
public class JwkSetService {

    private RestClient restClient;
    private JwkSetResponse jwkSetResponse;
    private RSAPublicKey publicKey;

    public RestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public JwkSetResponse getJwkSetResponse() {
        return jwkSetResponse;
    }

    public void setJwkSetResponse(JwkSetResponse jwkSetResponse) {
        this.jwkSetResponse = jwkSetResponse;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public void google(String token) {
        String baseUrl = "https://www.googleapis.com";
        String uri = "/oauth2/v3/certs";

        try {
            JWT jwt = JWTParser.parse(token);

            String kid = (String) jwt.getHeader().toJSONObject().get("kid");

            if (kid == null) {
                throw new InvalidBearerTokenException("Invalid token");
            }

            retrievePublicKey(baseUrl, uri, kid);

        } catch (Exception e) {
            throw new InvalidBearerTokenException("Invalid token");
        }
    }

    private void retrievePublicKey(String baseUrl, String uri, String kid) throws Exception {
        JwkSet jwkSet = fetchJwkSet(baseUrl, uri, kid);
        publicKey = generateRSAPublicKey(jwkSet);
    }

    private JwkSet fetchJwkSet(String baseUrl, String uri, String kid) throws Exception {
        restClient = RestClient.create(baseUrl);

        jwkSetResponse = restClient.get()
                .uri(uri)
                .retrieve()
                .body(JwkSetResponse.class);

        if (jwkSetResponse == null) {
            throw new AccessDeniedException("Couldn't get jwks");
        }

        return findJwkSetByKid(jwkSetResponse, kid);
    }

    private JwkSet findJwkSetByKid(JwkSetResponse jwkSetResponse, String kid) {
        return jwkSetResponse.getKeys()
                .stream()
                .filter(jwks -> kid.equals(jwks.getKid()))
                .findFirst()
                .orElseThrow(() -> new InvalidKeyException("Failed to retrieve jwks"));
    }

    private RSAPublicKey generateRSAPublicKey(JwkSet jwkSet) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(jwkSet.getN());
        byte[] exponentBytes = Base64.getUrlDecoder().decode(jwkSet.getE());

        RSAPublicKeySpec spec = new RSAPublicKeySpec(
                new BigInteger(1, modulusBytes),
                new BigInteger(1, exponentBytes));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }


}
