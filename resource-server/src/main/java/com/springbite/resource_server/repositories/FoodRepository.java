package com.springbite.resource_server.repositories;

import com.springbite.resource_server.models.Cuisine;
import com.springbite.resource_server.models.Food;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByName(String name);

    Optional<List<Food>> findTop5ByAvailable(
            Boolean available,
            Sort sort
    );

    Optional<List<Food>> findByAvailableAndRecommended(
            Boolean available,
            Boolean recommended,
            Sort sort
    );

    Optional<List<Food>> findByAvailableAndPriceBetweenAndAverageRatingBetween(
            Boolean available,
            Double minPrice,
            Double maxPrice,
            Double minAverageRating,
            Double maxAverageRating,
            Sort sort
    );

    Optional<List<Food>> findByAvailableAndCuisineAndPriceBetweenAndAverageRatingBetween(
            Boolean available,
            Cuisine cuisine,
            Double minPrice,
            Double maxPrice,
            Double minAverageRating,
            Double maxAverageRating,
            Sort sort
    );

    Optional<List<Food>> findByAvailableAndNameContainingIgnoreCaseAndPriceBetweenAndAverageRatingBetween(
            Boolean available,
            String name,
            Double minPrice,
            Double maxPrice,
            Double minAverageRating,
            Double maxAverageRating,
            Sort sort
    );

    Optional<List<Food>> findByAvailableAndNameContainingIgnoreCaseAndCuisineAndPriceBetweenAndAverageRatingBetween(
            Boolean available,
            String name,
            Cuisine cuisine,
            Double minPrice,
            Double maxPrice,
            Double minAverageRating,
            Double maxAverageRating,
            Sort sort
    );
}
