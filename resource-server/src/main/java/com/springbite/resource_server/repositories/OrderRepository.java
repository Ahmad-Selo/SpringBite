package com.springbite.resource_server.repositories;

import com.springbite.resource_server.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Set<Order>> findByUsername(String username);

    Optional<Order> findByCreatedAt(Date createdAt);
}
