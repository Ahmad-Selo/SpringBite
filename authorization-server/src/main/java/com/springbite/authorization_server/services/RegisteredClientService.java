package com.springbite.authorization_server.services;

import com.springbite.authorization_server.entities.RegisteredClientEntity;
import com.springbite.authorization_server.repositories.CustomRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisteredClientService implements RegisteredClientRepository {

    private final CustomRegisteredClientRepository customRegisteredClientRepository;

    public RegisteredClientService(CustomRegisteredClientRepository registeredClientRepository) {
        this.customRegisteredClientRepository = registeredClientRepository;
    }

    private RegisteredClientEntity toEntity(RegisteredClient registeredClient) {
        RegisteredClientEntity entity = new RegisteredClientEntity();
        entity.setClientId(
                registeredClient.getClientId());
        entity.setClientSecret(
                registeredClient.getClientSecret());
        entity.setClientAuthenticationMethods(
                registeredClient.getClientAuthenticationMethods());
        entity.setAuthorizationGrantTypes(
                registeredClient.getAuthorizationGrantTypes());
        entity.setRedirectUris(
                registeredClient.getRedirectUris());
        entity.setScopes(
                registeredClient.getScopes());

        return entity;
    }

    private RegisteredClient toRegisteredClient(RegisteredClientEntity entity) {
        return RegisteredClient.withId(String.valueOf(entity.getId()))
                .clientId(
                        entity.getClientId())
                .clientSecret(
                        entity.getClientSecret())
                .clientAuthenticationMethods(
                        authMethods -> authMethods.addAll(entity.getClientAuthenticationMethods()))
                .authorizationGrantTypes(
                        grantTypes -> grantTypes.addAll(entity.getAuthorizationGrantTypes()))
                .redirectUris(
                        redirectUris -> redirectUris.addAll(entity.getRedirectUris()))
                .scopes(
                        scopes -> scopes.addAll(entity.getScopes()))
                .build();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        RegisteredClientEntity entity = toEntity(registeredClient);
        customRegisteredClientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        return customRegisteredClientRepository.findById(Integer.valueOf(id))
                .map(this::toRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return customRegisteredClientRepository.findByClientId(clientId)
                .map(this::toRegisteredClient)
                .orElse(null);
    }
}
