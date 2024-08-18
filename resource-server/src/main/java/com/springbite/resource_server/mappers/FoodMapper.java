package com.springbite.resource_server.mappers;

import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.dtos.FoodDto;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    public Food foodDtoToFood(FoodDto dto) {
        return new Food(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getRecommended(),
                dto.getCuisine()
        );
    }
}
