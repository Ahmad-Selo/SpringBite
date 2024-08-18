package com.springbite.resource_server.repositories;

import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<List<Rating>> findByFood(Food food);
}
