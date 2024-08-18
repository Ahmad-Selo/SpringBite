package com.springbite.resource_server.services;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.exceptions.ValueOutOfRangeException;
import com.springbite.resource_server.mappers.FoodMapper;
import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.dtos.FoodDto;
import com.springbite.resource_server.repositories.FoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class FoodService {

    private final FoodRepository foodRepository;

    private final FoodMapper foodMapper;

    private final RatingService ratingService;

    public FoodService(
            FoodRepository foodRepository,
            FoodMapper foodMapper,
            RatingService ratingService
    ) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
        this.ratingService = ratingService;
    }

    public ResponseEntity<?> addFood(FoodDto dto) {
        Food food = foodMapper.foodDtoToFood(dto);

        foodRepository.save(food);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections
                .singletonMap("message", "New food item added successfully!"));
    }

    public ResponseEntity<?> deleteFood(String foodName) {
        try {
            Food food = foodRepository.findByName(foodName)
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found."));
            foodRepository.delete(food);

            return ResponseEntity.status(HttpStatus.OK).body(Collections
                    .singletonMap("message", "Food deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> addRating(String foodName, String username, Double ratingValue) {
        try {
            Food food = foodRepository.findByName(foodName)
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

            ratingService.saveRating(food, username, ratingValue);

            updateAverageRating(food);

        } catch (ResourceNotFoundException | ValueOutOfRangeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Thank you for your rating!"));
    }

    public void updateAverageRating(Food food) throws ResourceNotFoundException {
        double averageRating = ratingService.getAverageRating(food);

        food.setAverageRating(averageRating);

        foodRepository.save(food);
    }
}
