package com.springbite.resource_server.controllers;

import com.springbite.resource_server.models.dtos.FoodDto;
import com.springbite.resource_server.services.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/food")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addFood(@Valid @RequestBody FoodDto dto) {
        return foodService.addFood(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{food_name}")
    public ResponseEntity<?> deleteFood(
            @PathVariable("food_name") String foodName
    ) {
        return foodService.deleteFood(foodName);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{food_name}/rate")
    public ResponseEntity<?> rateFood(
            Authentication authentication,
            @PathVariable("food_name") String foodName,
            @RequestParam("rating_value") Double ratingValue
    ) {
        return foodService.addRating(foodName, authentication.getName(), ratingValue);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        var errors = new ArrayList<>();

        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                .singletonMap("error", errors));
    }
}
