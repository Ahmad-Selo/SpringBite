package com.springbite.client_server.repositories.impls;

import com.springbite.client_server.mappers.ClientRegistrationMapper;
import com.springbite.client_server.repositories.CustomClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public class ClientRegistrationRepositoryImpl
        implements ClientRegistrationRepository, Iterable<ClientRegistration> {

    private final CustomClientRegistrationRepository customClientRegistrationRepository;

    private final ClientRegistrationMapper clientRegistrationMapper;

    public ClientRegistrationRepositoryImpl(CustomClientRegistrationRepository customClientRegistrationRepository, ClientRegistrationMapper clientRegistrationMapper) {
        this.customClientRegistrationRepository = customClientRegistrationRepository;
        this.clientRegistrationMapper = clientRegistrationMapper;
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return customClientRegistrationRepository.findByRegistrationId(registrationId)
                .map(clientRegistrationMapper::toClientRegistration)
                .orElse(null);
    }

    @Override
    public Iterator<ClientRegistration> iterator() {
        return customClientRegistrationRepository.findAll()
                .stream()
                .map(clientRegistrationMapper::toClientRegistration)
                .iterator();
    }
}
