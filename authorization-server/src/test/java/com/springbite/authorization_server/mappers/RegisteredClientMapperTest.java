package com.springbite.authorization_server.mappers;

import com.springbite.authorization_server.entities.RegisteredClientEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisteredClientMapperTest {

    private RegisteredClientMapper registeredClientMapper;

    @BeforeEach
    void setUp() {
        this.registeredClientMapper = new RegisteredClientMapper();
    }

    @Test
    void RegisteredClientToRegisteredClientEntityTest() {
        RegisteredClient registeredClient =
                RegisteredClient.withId("1")
                        .clientId("client")
                        .clientSecret("secret")
                        .clientAuthenticationMethod(
                                ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                        )
                        .authorizationGrantType(
                                AuthorizationGrantType.AUTHORIZATION_CODE
                        )
                        .redirectUri(
                                "http://localhost:8080/login/oauth2/code/springbite"
                        )
                        .scope(OidcScopes.OPENID)
                        .build();

        RegisteredClientEntity registeredClientEntity = registeredClientMapper
                .toRegisteredClientEntity(registeredClient);

        assertEquals(registeredClient.getClientId(), registeredClientEntity.getClientId());

        assertEquals(registeredClient.getClientSecret(), registeredClientEntity.getClientSecret());

        assertEquals(
                registeredClient.getClientAuthenticationMethods()
                        .stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.toSet()),
                registeredClientEntity.getClientAuthenticationMethods()
        );

        assertEquals(
                registeredClient.getAuthorizationGrantTypes()
                        .stream()
                        .map(AuthorizationGrantType::getValue)
                        .collect(Collectors.toSet()),
                registeredClientEntity.getAuthorizationGrantTypes()
        );

        assertEquals(
                registeredClient.getRedirectUris(),
                registeredClientEntity.getRedirectUris()
        );

        assertEquals(
                registeredClient.getScopes(),
                registeredClientEntity.getScopes()
        );
    }

    @Test
    void RegisteredClientEntityToRegisteredClientTest() {
        RegisteredClientEntity entity =
                new RegisteredClientEntity(
                        "client",
                        "secret",
                        Set.of("client_secret_basic"),
                        Set.of("authorization_code"),
                        Set.of("http://localhost:8080/login/oauth2/code/springbite"),
                        Set.of("openid")
                );

        RegisteredClient registeredClient = registeredClientMapper
                .toRegisteredClient(entity);

        assertEquals(entity.getClientId(), registeredClient.getClientId());

        assertEquals(entity.getClientSecret(), registeredClient.getClientSecret());

        assertEquals(
                entity.getClientAuthenticationMethods(),
                registeredClient.getClientAuthenticationMethods()
                        .stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.toSet())
        );

        assertEquals(
                entity.getAuthorizationGrantTypes(),
                registeredClient.getAuthorizationGrantTypes()
                        .stream()
                        .map(AuthorizationGrantType::getValue)
                        .collect(Collectors.toSet())
        );

        assertEquals(
                entity.getRedirectUris(),
                registeredClient.getRedirectUris()
        );

        assertEquals(
                entity.getScopes(),
                registeredClient.getScopes()
        );

    }
}