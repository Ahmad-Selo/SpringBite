package com.springbite.authorization_server.repositories;

import com.springbite.authorization_server.entities.RegisteredClientEntity;
import com.springbite.authorization_server.mappers.RegisteredClientMapper;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final JpaRegisteredClientRepository jpaRegisteredClientRepository;

    private final RegisteredClientMapper registeredClientMapper;

    public CustomRegisteredClientRepository(JpaRegisteredClientRepository registeredClientRepository, RegisteredClientMapper registeredClientMapper) {
        this.jpaRegisteredClientRepository = registeredClientRepository;
        this.registeredClientMapper = registeredClientMapper;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        RegisteredClientEntity entity = registeredClientMapper.toRegisteredClientEntity(registeredClient);
        jpaRegisteredClientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        return jpaRegisteredClientRepository.findById(Integer.valueOf(id))
                .map(registeredClientMapper::toRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return jpaRegisteredClientRepository.findByClientId(clientId)
                .map(registeredClientMapper::toRegisteredClient)
                .orElse(null);
    }
}
