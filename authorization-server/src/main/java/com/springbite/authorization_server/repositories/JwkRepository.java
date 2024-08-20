package com.springbite.authorization_server.repositories;

import com.springbite.authorization_server.entities.JwkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwkRepository extends JpaRepository<JwkEntity, String> {
}
