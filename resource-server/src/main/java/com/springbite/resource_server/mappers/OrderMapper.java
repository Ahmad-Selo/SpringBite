package com.springbite.resource_server.mappers;

import com.springbite.resource_server.models.Order;
import com.springbite.resource_server.models.dtos.OrderResponseDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderResponseDto orderToOrderResponseDto(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getCreatedAt(),
                order.getOrderItems()
                        .stream()
                        .map(orderItemMapper::orderItemToOrderItemResponseDto)
                        .collect(Collectors.toList()),
                order.getAddress(),
                order.getPrice()
        );
    }
}
