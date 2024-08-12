package com.springbite.authorization_server.services;

import com.springbite.authorization_server.entities.RegisteredClientEntity;
import com.springbite.authorization_server.repositories.JpaRegisteredClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RegisteredClientService implements RegisteredClientRepository {

    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    public RegisteredClientService(JpaRegisteredClientRepository registeredClientRepository) {
        this.jpaRegisteredClientRepository = registeredClientRepository;
    }

    private RegisteredClientEntity toEntity(RegisteredClient registeredClient) {
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

    private RegisteredClient toRegisteredClient(RegisteredClientEntity entity) {
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

                .redirectUris(
                        redirectUris -> redirectUris.addAll(entity.getRedirectUris()))

                .scopes(
                        scopes -> scopes.addAll(entity.getScopes()))

                .build();
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        RegisteredClientEntity entity = toEntity(registeredClient);
        jpaRegisteredClientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        return jpaRegisteredClientRepository.findById(Integer.valueOf(id))
                .map(this::toRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return jpaRegisteredClientRepository.findByClientId(clientId)
                .map(this::toRegisteredClient)
                .orElse(null);
    }
}
