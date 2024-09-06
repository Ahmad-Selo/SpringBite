package com.springbite.resource_server.mappers;

import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.OrderItem;
import com.springbite.resource_server.models.dtos.OrderItemDto;
import com.springbite.resource_server.models.dtos.OrderItemResponseDto;
import com.springbite.resource_server.repositories.FoodRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    private final FoodRepository foodRepository;

    public OrderItemMapper(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto) {
        Food food = foodRepository.findById(orderItemDto.getFoodId())
                .orElseThrow();

        return new OrderItem(
                food,
                food.getPrice(),
                orderItemDto.getQuantity()
        );
    }

    public OrderItemResponseDto orderItemToOrderItemResponseDto(OrderItem orderItem) {
        return new OrderItemResponseDto(
                orderItem.getFood().getName(),
                orderItem.getItemPrice(),
                orderItem.getQuantity()
        );
    }

}
