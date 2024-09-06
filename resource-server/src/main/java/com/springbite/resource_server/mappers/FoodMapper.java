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
                dto.getPicture(),
                dto.getDescription(),
                dto.getPrice(),
                dto.isAvailable(),
                dto.isRecommended(),
                dto.getCuisine()
        );
    }

    public AvailableFoodResponse foodToAvailableFoodResponse(Food food) {
        return new AvailableFoodResponse(
                food.getId(),
                food.getName(),
                food.getPicture(),
                food.getPrice(),
                food.getAverageRating(),
                food.isRecommended()
        );
    }

    public FoodResponseDto foodToFoodResponseDto(Food food) {
        return new FoodResponseDto(
                food.getId(),
                food.getName(),
                food.getPicture(),
                food.getDescription(),
                food.getAverageRating(),
                food.getPopularity(),
                food.getPrice(),
                food.isRecommended()
        );
    }
}
