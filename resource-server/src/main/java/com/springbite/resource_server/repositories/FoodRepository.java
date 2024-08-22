package com.springbite.resource_server.repositories;

import com.springbite.resource_server.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByName(String name);

    Optional<Set<Food>> findByAvailable(Boolean available);
}
