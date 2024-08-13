package com.springbite.client_server.repositories;

import com.springbite.client_server.entities.ClientRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomClientRegistrationRepository extends JpaRepository<ClientRegistrationEntity, Long> {
    Optional<ClientRegistrationEntity> findByRegistrationId(String registrationId);
}
