package com.springbite.authorization_server.repositories;

import com.springbite.authorization_server.entities.RegisteredClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRegisteredClientRepository extends JpaRepository<RegisteredClientEntity, Long> {
    Optional<RegisteredClientEntity> findByClientId(String clientId);
}
