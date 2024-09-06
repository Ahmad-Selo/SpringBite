package com.springbite.resource_server.controllers;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.exceptions.ValueOutOfRangeException;
import com.springbite.resource_server.models.dtos.FoodDto;
import com.springbite.resource_server.models.dtos.UpdateFoodRequest;
import com.springbite.resource_server.security.CustomAuthentication;
import com.springbite.resource_server.security.HasAnyRole;
import com.springbite.resource_server.security.HasRole;
import com.springbite.resource_server.services.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/highlights")
    public ResponseEntity<?> highlightsFoods() {
        return foodService.getHighlightsFoods();
    }

    @GetMapping("/available")
    public ResponseEntity<?> availableFoods(
            @RequestParam(value = "cuisine", required = false) String cuisine,
            @RequestParam(value = "min-price", required = false, defaultValue = "0.0") Double minPrice,
            @RequestParam(value = "max-price", required = false, defaultValue = "1.7976931348623157e+308") Double maxPrice,
            @RequestParam(value = "min-average-rating", required = false, defaultValue = "0.0") Double minAverageRating,
            @RequestParam(value = "max-average-rating", required = false, defaultValue = "5.0") Double maxAverageRating,
            @RequestParam(value = "sort-by", required = false, defaultValue = "averageRating") String sortBy,
            @RequestParam(value = "sort-type", required = false, defaultValue = "Desc") String sortType
    ) {
        return foodService.availableFoods(
                cuisine,
                minPrice,
                maxPrice,
                minAverageRating,
                maxAverageRating,
                sortBy,
                sortType
        );
    }

    @GetMapping("/available/{food-id}")
    public ResponseEntity<?> getAvailableFood(
            @PathVariable("food-id") Long foodId
    ) throws ResourceNotFoundException {
        return foodService.getAvailableFood(foodId);
    }

    @GetMapping("/available/search")
    public ResponseEntity<?> searchAvailableFoods(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "cuisine", required = false) String cuisine,
            @RequestParam(value = "min-price", required = false, defaultValue = "0.0") Double minPrice,
            @RequestParam(value = "max-price", required = false, defaultValue = "1.7976931348623157e+308") Double maxPrice,
            @RequestParam(value = "min-average-rating", required = false, defaultValue = "0.0") Double minAverageRating,
            @RequestParam(value = "max-average-rating", required = false, defaultValue = "5.0") Double maxAverageRating,
            @RequestParam(value = "sort-by", required = false, defaultValue = "averageRating") String sortBy,
            @RequestParam(value = "sort-type", required = false, defaultValue = "Desc") String sortType
    ) {
        return foodService.searchAvailableFoods(
                query,
                cuisine,
                minPrice,
                maxPrice,
                minAverageRating,
                maxAverageRating,
                sortBy,
                sortType
        );
    }

    @GetMapping("/{food-id}")
    @HasAnyRole(roles = {"'ADMIN'", "'MANAGER'"})
    public ResponseEntity<?> getFood(
            @PathVariable("food-id") Long foodId
    ) throws ResourceNotFoundException {
        return foodService.getFood(foodId);
    }

    @PostMapping("/add")
    @HasAnyRole(roles = {"'ADMIN'", "'MANAGER'"})
    public ResponseEntity<?> addFood(@Valid @RequestBody FoodDto dto) {
        return foodService.addFood(dto);
    }

    @PatchMapping("/{food-id}/update")
    @HasAnyRole(roles = {"'ADMIN'", "'MANAGER'"})
    public ResponseEntity<?> updateFood(
            @PathVariable("food-id") Long foodId,
            @Valid @RequestBody UpdateFoodRequest updateFoodRequest
    ) throws ResourceNotFoundException {
        return foodService.updateFood(foodId, updateFoodRequest);
    }

    @DeleteMapping("/{food-id}/delete")
    @HasAnyRole(roles = {"'ADMIN'", "'MANAGER'"})
    public ResponseEntity<?> deleteFood(
            @PathVariable("food-id") Long foodId
    ) throws ResourceNotFoundException {
        return foodService.deleteFood(foodId);
    }

    @PostMapping("/{food-id}/rate")
    @HasRole("USER")
    public ResponseEntity<?> rateFood(
            @PathVariable("food-id") Long foodId,
            @RequestParam("rating-value") Double ratingValue,
            CustomAuthentication customAuthentication
    ) throws ResourceNotFoundException, ValueOutOfRangeException {
        return foodService.rateFood(foodId, customAuthentication.getUserId(), ratingValue);
    }
}
