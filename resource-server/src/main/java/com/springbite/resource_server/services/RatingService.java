package com.springbite.resource_server.services;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.Rating;
import com.springbite.resource_server.repositories.FoodRepository;
import com.springbite.resource_server.repositories.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    private final FoodRepository foodRepository;

    private final FoodService foodService;

    public RatingService(
            RatingRepository ratingRepository,
            FoodRepository foodRepository,
            FoodService foodService) {
        this.ratingRepository = ratingRepository;
        this.foodRepository = foodRepository;
        this.foodService = foodService;
    }

    public ResponseEntity<?> addRating(String name, Authentication authentication, double ratingValue) {
        try {
            Food food = foodRepository
                    .findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found"));

            Rating rating = new Rating(
                    food,
                    authentication.getName(),
                    ratingValue
            );

            ratingRepository.save(rating);

            foodService.updateAverageRating(food);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Thank you for your rating!");
    }

}
