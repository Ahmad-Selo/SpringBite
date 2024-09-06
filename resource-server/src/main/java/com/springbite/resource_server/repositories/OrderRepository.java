package com.springbite.resource_server.repositories;

import com.springbite.resource_server.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByUserId(Long userId);

    Optional<Order> findByCreatedAt(Date createdAt);
}
