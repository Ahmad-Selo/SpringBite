package com.springbite.authorization_server.repositories.impls;

import com.springbite.authorization_server.entities.RegisteredClientEntity;
import com.springbite.authorization_server.mappers.RegisteredClientMapper;
import com.springbite.authorization_server.repositories.CustomRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {

    private final CustomRegisteredClientRepository customRegisteredClientRepository;

    private final RegisteredClientMapper registeredClientMapper;

    public RegisteredClientRepositoryImpl(CustomRegisteredClientRepository registeredClientRepository, RegisteredClientMapper registeredClientMapper) {
        this.customRegisteredClientRepository = registeredClientRepository;
        this.registeredClientMapper = registeredClientMapper;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        RegisteredClientEntity entity = registeredClientMapper.toRegisteredClientEntity(registeredClient);
        customRegisteredClientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        return customRegisteredClientRepository.findById(Long.valueOf(id))
                .map(registeredClientMapper::toRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return customRegisteredClientRepository.findByClientId(clientId)
                .map(registeredClientMapper::toRegisteredClient)
                .orElse(null);
    }
}
