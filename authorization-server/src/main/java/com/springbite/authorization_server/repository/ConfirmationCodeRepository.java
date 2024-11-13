package com.springbite.authorization_server.repository;

import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.entity.VerificationCode;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByCode(String code);

    Optional<List<VerificationCode>> findByUser(User user, Sort sort);
}
