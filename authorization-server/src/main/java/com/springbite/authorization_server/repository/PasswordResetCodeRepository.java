package com.springbite.authorization_server.repository;

import com.springbite.authorization_server.entity.PasswordResetCode;
import com.springbite.authorization_server.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    Optional<PasswordResetCode> findByCode(String code);

    Optional<List<PasswordResetCode>> findByUser(User user, Sort sort);
}
