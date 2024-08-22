package com.springbite.resource_server.services;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.exceptions.ValueOutOfRangeException;
import com.springbite.resource_server.mappers.FoodMapper;
import com.springbite.resource_server.models.Cuisine;
import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.dtos.AvailableFoodResponse;
import com.springbite.resource_server.models.dtos.FoodDto;
import com.springbite.resource_server.models.dtos.FoodResponseDto;
import com.springbite.resource_server.models.dtos.UpdateFoodRequest;
import com.springbite.resource_server.repositories.FoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public ResponseEntity<?> allFoodAvailable() {
        Set<Food> availableFoods = foodRepository.findByAvailable(true).orElse(null);

        Set<AvailableFoodResponse> availableFoodResponseSet;

        if (availableFoods != null) {
            availableFoodResponseSet = availableFoods
                    .stream()
                    .map(foodMapper::foodToAvailableFoodResponse)
                    .collect(Collectors.toSet());
        } else {
            availableFoodResponseSet = new HashSet<>();
        }

        return ResponseEntity.status(HttpStatus.OK).body(availableFoodResponseSet);
    }

    public ResponseEntity<?> getFood(Long foodId, Authentication authentication) {
        try {
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

            if (!food.getAvailable() &&
                    authentication.getAuthorities()
                            .contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                        .singletonMap("error", "Food not available."));
            }

            FoodResponseDto foodResponseDto = foodMapper.foodToFoodResponseDto(food);

            return ResponseEntity.status(HttpStatus.OK).body(foodResponseDto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> addFood(FoodDto dto) {
        Food food = foodMapper.foodDtoToFood(dto);

        foodRepository.save(food);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections
                .singletonMap("message", "New food added successfully!"));
    }

    public ResponseEntity<?> updateFood(Long foodId, UpdateFoodRequest updateFoodRequest) {
        try {
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

            String name = updateFoodRequest.getName();
            String description = updateFoodRequest.getDescription();
            Double price = updateFoodRequest.getPrice();
            Boolean available = updateFoodRequest.getAvailable();
            Boolean recommend = updateFoodRequest.getRecommend();
            Cuisine cuisine = updateFoodRequest.getCuisine();

            if (name != null && !name.isBlank()) {
                food.setName(name);
            }

            if (description != null && !description.isBlank()) {
                food.setDescription(description);
            }

            if (price != null) {
                food.setPrice(price);
            }

            if (available != null) {
                food.setAvailable(available);
            }

            if (recommend != null) {
                food.setRecommend(recommend);
            }

            if (cuisine != null) {
                food.setCuisine(cuisine);
            }

            foodRepository.save(food);

            return ResponseEntity.status(HttpStatus.OK).body(Collections
                    .singletonMap("message", "Food details updated successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> deleteFood(Long foodId) {
        try {
            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

            foodRepository.delete(food);

            return ResponseEntity.status(HttpStatus.OK).body(Collections
                    .singletonMap("message", "Food deleted successfully!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> rateFood(Long foodId, String username, Double ratingValue) {
        try {
            Food food = foodRepository.findById(foodId)
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

    private void updateAverageRating(Food food) throws ResourceNotFoundException {
        double averageRating = ratingService.getAverageRating(food);

        food.setAverageRating(averageRating);

        foodRepository.save(food);
    }
}
