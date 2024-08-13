package com.springbite.client_server.mappers;

import com.springbite.client_server.entities.ClientRegistrationEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

@Component
public class ClientRegistrationMapper {

    public ClientRegistration toClientRegistration(ClientRegistrationEntity entity) {
        return ClientRegistration.withRegistrationId(entity.getRegistrationId())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .clientAuthenticationMethod(new ClientAuthenticationMethod(
                        entity.getClientAuthenticationMethod()
                ))
                .authorizationGrantType(new AuthorizationGrantType(
                        entity.getAuthorizationGrantType()
                ))
                .redirectUri(entity.getRedirectUri())
                .scope(entity.getScopes())
                .clientName(entity.getClientName())
                .build();
    }

}
