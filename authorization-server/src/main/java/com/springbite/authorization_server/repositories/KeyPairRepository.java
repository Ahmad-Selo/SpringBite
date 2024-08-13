package com.springbite.authorization_server.repositories;

import com.springbite.authorization_server.entities.KeyPairEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyPairRepository extends JpaRepository<KeyPairEntity, Long> {
}
