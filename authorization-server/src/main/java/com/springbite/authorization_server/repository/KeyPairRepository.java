package com.springbite.authorization_server.repository;

import com.springbite.authorization_server.entity.KeyPairEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeyPairRepository extends JpaRepository<KeyPairEntity, Long> {
    Optional<KeyPairEntity> findByKid(String kid);
}
