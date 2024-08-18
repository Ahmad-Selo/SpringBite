package com.springbite.resource_server.services;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.Rating;
import com.springbite.resource_server.repositories.FoodRepository;
import com.springbite.resource_server.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    private final RatingRepository ratingRepository;

    public FoodService(FoodRepository foodRepository, RatingRepository ratingRepository) {
        this.foodRepository = foodRepository;
        this.ratingRepository = ratingRepository;
    }

    public void updateAverageRating(Food food) throws ResourceNotFoundException {
        List<Rating> ratings = ratingRepository
                .findByFood(food)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found"));

        double averageRating = ratings.stream()
                .mapToDouble(Rating::getRatingValue)
                .average()
                .orElse(0.0);

        food.setAverageRating(averageRating);

        foodRepository.save(food);
    }
}
