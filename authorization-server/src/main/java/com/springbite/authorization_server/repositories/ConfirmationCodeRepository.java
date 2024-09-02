package com.springbite.authorization_server.repositories;

import com.springbite.authorization_server.entities.ConfirmationCode;
import com.springbite.authorization_server.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    Optional<ConfirmationCode> findByCode(String code);

    Optional<List<ConfirmationCode>> findByUser(User user, Sort sort);
}
