package com.springbite.authorization_server.repository;

import com.springbite.authorization_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrPhoneNumber(String username, String phoneNumber);

    Optional<List<User>> findByUsernameContaining(String username);

    boolean existsByUsername(String username);

    Optional<List<User>> findByPhoneNumberContaining(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);
}
