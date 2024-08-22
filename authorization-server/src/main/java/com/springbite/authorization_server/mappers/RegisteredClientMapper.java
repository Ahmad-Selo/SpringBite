package com.springbite.authorization_server.mappers;

import com.springbite.authorization_server.entities.RegisteredClientEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.stream.Collectors;

@Component
public class RegisteredClientMapper {

    public RegisteredClientEntity toRegisteredClientEntity(RegisteredClient registeredClient) {
        RegisteredClientEntity entity = new RegisteredClientEntity();

        entity.setClientId(
                registeredClient.getClientId());

        entity.setClientSecret(
                registeredClient.getClientSecret());

        entity.setClientAuthenticationMethods(
                registeredClient.getClientAuthenticationMethods()
                        .stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .collect(Collectors.toSet()));

        entity.setAuthorizationGrantTypes(
                registeredClient.getAuthorizationGrantTypes()
                        .stream()
                        .map(AuthorizationGrantType::getValue)
                        .collect(Collectors.toSet()));

        entity.setRedirectUris(
                registeredClient.getRedirectUris());

        entity.setScopes(
                registeredClient.getScopes());

        return entity;
    }

    public RegisteredClient toRegisteredClient(RegisteredClientEntity entity) {
        return RegisteredClient.withId(String.valueOf(entity.getId()))
                .clientId(
                        entity.getClientId())
                .clientSecret(
                        entity.getClientSecret())

                .clientAuthenticationMethods(
                        authMethods -> authMethods.addAll(entity.getClientAuthenticationMethods()
                                .stream()
                                .map(ClientAuthenticationMethod::new)
                                .collect(Collectors.toSet())))

                .authorizationGrantTypes(
                        grantTypes -> grantTypes.addAll(entity.getAuthorizationGrantTypes()
                                .stream()
                                .map(AuthorizationGrantType::new)
                                .collect(Collectors.toSet())))

                .tokenSettings(
                        TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofHours(1))
                                .build()
                )

                .redirectUris(
                        redirectUris -> redirectUris.addAll(entity.getRedirectUris()))

                .scopes(
                        scopes -> scopes.addAll(entity.getScopes()))

                .build();
    }

}
