package com.springbite.client_server.mappers;

import com.springbite.client_server.entities.ClientRegistrationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientRegistrationMapperTest {

    private ClientRegistrationMapper clientRegistrationMapper;

    @BeforeEach
    void setUp() {
        clientRegistrationMapper = new ClientRegistrationMapper();
    }

    @Test
    void clientRegistrationEntityToClientRegistration() {
        ClientRegistrationEntity entity = new ClientRegistrationEntity();

        entity.setRegistrationId("springbite");
        entity.setClientId("client");
        entity.setClientSecret("secret");
        entity.setClientAuthenticationMethod("client_secret_basic");
        entity.setAuthorizationGrantType("authorization_code");
        entity.setRedirectUri("http://localhost:8080");
        entity.setScopes(Set.of("openid"));
        entity.setClientName("springbite");
        entity.setAuthorizationUri("http://localhost:8080/authorize");
        entity.setTokenUri("http://localhost:8080/token");
        entity.setJwkSetUri("http://localhost:8080/jwk");
        entity.setIssuerUri("http://localhost:8080");
        entity.setUserInfoUri("http://localhost:8080/user");
        entity.setUserNameAttributeName("user");

        ClientRegistration clientRegistration =
                clientRegistrationMapper.toClientRegistration(entity);

        assertEquals(entity.getRegistrationId(), clientRegistration.getRegistrationId());

        assertEquals(entity.getClientId(), clientRegistration.getClientId());

        assertEquals(entity.getClientSecret(), clientRegistration.getClientSecret());

        assertEquals(entity.getClientAuthenticationMethod(),
                clientRegistration.getClientAuthenticationMethod().getValue());

        assertEquals(entity.getAuthorizationGrantType(),
                clientRegistration.getAuthorizationGrantType().getValue());

        assertEquals(entity.getRedirectUri(), clientRegistration.getRedirectUri());

        assertEquals(entity.getScopes(), clientRegistration.getScopes());

        assertEquals(entity.getClientName(), clientRegistration.getClientName());

        assertEquals(entity.getAuthorizationUri(),
                clientRegistration.getProviderDetails().getAuthorizationUri());

        assertEquals(entity.getTokenUri(),
                clientRegistration.getProviderDetails().getTokenUri());

        assertEquals(entity.getJwkSetUri(),
                clientRegistration.getProviderDetails().getJwkSetUri());

        assertEquals(entity.getIssuerUri(),
                clientRegistration.getProviderDetails().getIssuerUri());

        assertEquals(entity.getUserInfoUri(),
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri());

        assertEquals(entity.getUserNameAttributeName(),
                clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());

    }
}