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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    public ResponseEntity<?> getHighlightsFoods() {
        List<AvailableFoodResponse> newestFoods = foodRepository
                .findTop5ByAvailable(true, Sort.by(Sort.Direction.DESC, "createdAt"))
                .orElse(new ArrayList<>())
                .stream()
                .map(foodMapper::foodToAvailableFoodResponse)
                .collect(Collectors.toList());

        List<AvailableFoodResponse> popularFoods = foodRepository
                .findTop5ByAvailable(true, Sort.by(Sort.Direction.DESC, "popularity"))
                .orElse(new ArrayList<>())
                .stream()
                .map(foodMapper::foodToAvailableFoodResponse)
                .collect(Collectors.toList());

        List<AvailableFoodResponse> recommendedFoods = foodRepository
                .findByAvailableAndRecommended(true, true, Sort.by(Sort.Direction.DESC, "averageRating"))
                .orElse(new ArrayList<>())
                .stream()
                .map(foodMapper::foodToAvailableFoodResponse)
                .collect(Collectors.toList());

        Map<String, Object> body = Map.of(
                "newest", newestFoods,
                "popular", popularFoods,
                "recommended", recommendedFoods
        );

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public ResponseEntity<?> availableFoods(
            String cuisine,
            Double minPrice,
            Double maxPrice,
            Double minAverageRating,
            Double maxAverageRating,
            String sortBy,
            String sortType
    ) {
        List<Food> availableFoods;

        Sort sort = Sort.by(Sort.Direction.fromString(sortType), sortBy);

        if (cuisine == null) {
            availableFoods = foodRepository.findByAvailableAndPriceBetweenAndAverageRatingBetween(
                    true,
                    minPrice,
                    maxPrice,
                    minAverageRating,
                    maxAverageRating,
                    sort
            ).orElse(new ArrayList<>());
        } else {
            Cuisine cuisineEnum = Cuisine.valueOf(cuisine);

            availableFoods = foodRepository.findByAvailableAndCuisineAndPriceBetweenAndAverageRatingBetween(
                    true,
                    cuisineEnum,
                    minPrice,
                    maxPrice,
                    minAverageRating,
                    maxAverageRating,
                    sort
            ).orElse(new ArrayList<>());
        }

        List<AvailableFoodResponse> availableFoodsResponse = availableFoods
                .stream()
                .map(foodMapper::foodToAvailableFoodResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(availableFoodsResponse);
    }

    public ResponseEntity<?> getAvailableFood(Long foodId) throws ResourceNotFoundException {
        Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        if (!food.isAvailable()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Food not available!"));
        }

        FoodResponseDto foodResponseDto = foodMapper.foodToFoodResponseDto(food);

        return ResponseEntity.status(HttpStatus.OK).body(foodResponseDto);
    }

    public ResponseEntity<?> searchAvailableFoods(
            String query,
            String cuisine,
            Double minPrice,
            Double maxPrice,
            Double minAverageRating,
            Double maxAverageRating,
            String sortBy,
            String sortType
    ) {
        List<Food> availableFoods;

        Sort sort = Sort.by(Sort.Direction.fromString(sortType), sortBy);

        if (cuisine == null) {
            availableFoods = foodRepository.findByAvailableAndNameContainingIgnoreCaseAndPriceBetweenAndAverageRatingBetween(
                    true,
                    query,
                    minPrice,
                    maxPrice,
                    minAverageRating,
                    maxAverageRating,
                    sort
            ).orElse(new ArrayList<>());
        } else {
            Cuisine cuisineEnum = Cuisine.valueOf(cuisine);

            availableFoods = foodRepository.findByAvailableAndNameContainingIgnoreCaseAndCuisineAndPriceBetweenAndAverageRatingBetween(
                    true,
                    query,
                    cuisineEnum,
                    minPrice,
                    maxPrice,
                    minAverageRating,
                    maxAverageRating,
                    sort
            ).orElse(new ArrayList<>());
        }

        List<AvailableFoodResponse> availableFoodsResponse = availableFoods
                .stream()
                .map(foodMapper::foodToAvailableFoodResponse)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(availableFoodsResponse);
    }

    public ResponseEntity<?> getFood(Long foodId) throws ResourceNotFoundException {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        FoodResponseDto foodResponseDto = foodMapper.foodToFoodResponseDto(food);

        return ResponseEntity.status(HttpStatus.OK).body(foodResponseDto);
    }

    public ResponseEntity<?> addFood(FoodDto dto) {
        Food food = foodMapper.foodDtoToFood(dto);

        foodRepository.save(food);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections
                .singletonMap("message", "New food added successfully!"));
    }

    public ResponseEntity<?> updateFood(Long foodId, UpdateFoodRequest updateFoodRequest)
            throws ResourceNotFoundException {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        String name = updateFoodRequest.getName();
        String description = updateFoodRequest.getDescription();
        Double price = updateFoodRequest.getPrice();
        Boolean available = updateFoodRequest.isAvailable();
        Boolean recommended = updateFoodRequest.isRecommended();
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

        if (recommended != null) {
            food.setRecommended(recommended);
        }

        if (cuisine != null) {
            food.setCuisine(cuisine);
        }

        foodRepository.save(food);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Food details updated successfully!"));
    }

    public ResponseEntity<?> deleteFood(Long foodId) throws ResourceNotFoundException {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        foodRepository.delete(food);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Food deleted successfully!"));
    }

    public ResponseEntity<?> rateFood(Long foodId, Long userId, Double ratingValue)
            throws ResourceNotFoundException, ValueOutOfRangeException {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food not found."));

        ratingService.saveRating(food, userId, ratingValue);

        updateAverageRating(food);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Thank you for your rating!"));
    }

    private void updateAverageRating(Food food) throws ResourceNotFoundException {
        double averageRating = ratingService.getAverageRating(food);

        food.setAverageRating(averageRating);

        foodRepository.save(food);
    }
}
