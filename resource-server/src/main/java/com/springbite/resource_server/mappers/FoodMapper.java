package com.springbite.resource_server.mappers;

import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.dtos.AvailableFoodResponse;
import com.springbite.resource_server.models.dtos.FoodDto;
import com.springbite.resource_server.models.dtos.FoodResponseDto;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    public Food foodDtoToFood(FoodDto dto) {
        return new Food(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getAvailable(),
                dto.getRecommend(),
                dto.getCuisine()
        );
    }

    public AvailableFoodResponse foodToAvailableFoodResponse(Food food) {
        return new AvailableFoodResponse(
                food.getId(),
                food.getName(),
                food.getPrice(),
                food.getAverageRating(),
                food.getCreatedAt(),
                food.getRecommend()
        );
    }

    public FoodResponseDto foodToFoodResponseDto(Food food) {
        return new FoodResponseDto(
                food.getId(),
                food.getName(),
                food.getDescription(),
                food.getAverageRating(),
                food.getPrice(),
                food.getRecommend()
        );
    }
}
