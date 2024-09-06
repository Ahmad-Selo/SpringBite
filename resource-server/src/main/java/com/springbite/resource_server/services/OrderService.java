package com.springbite.resource_server.services;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.mappers.OrderItemMapper;
import com.springbite.resource_server.mappers.OrderMapper;
import com.springbite.resource_server.models.Food;
import com.springbite.resource_server.models.Order;
import com.springbite.resource_server.models.OrderItem;
import com.springbite.resource_server.models.dtos.OrderDto;
import com.springbite.resource_server.models.dtos.OrderItemDto;
import com.springbite.resource_server.models.dtos.OrderResponseDto;
import com.springbite.resource_server.repositories.FoodRepository;
import com.springbite.resource_server.repositories.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final FoodRepository foodRepository;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    public OrderService(
            OrderRepository orderRepository,
            FoodRepository foodRepository,
            OrderMapper orderMapper,
            OrderItemMapper orderItemMapper
    ) {
        this.orderRepository = orderRepository;
        this.foodRepository = foodRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    public ResponseEntity<?> getAllOrders() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        List<Order> orders = orderRepository.findAll(sort);

        List<OrderResponseDto> ordersResponseDto = orders
                .stream()
                .map(orderMapper::orderToOrderResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(ordersResponseDto);
    }

    public ResponseEntity<?> getOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId)
                .orElse(new ArrayList<>());

        List<OrderResponseDto> ordersResponse = orders
                .stream()
                .map(orderMapper::orderToOrderResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(ordersResponse);
    }

    public ResponseEntity<?> getOrder(Long orderId) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        OrderResponseDto orderResponseDto = orderMapper.orderToOrderResponseDto(order);

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    public ResponseEntity<?> createOrder(OrderDto orderDto, Long userId) {
        Map<Long, String> invalidOrderItems = validateOrderAndCollectInvalidItems(orderDto);

        if (!invalidOrderItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidOrderItems);
        }

        Order order = new Order(userId, orderDto.getAddress());

        orderRepository.save(order);

        List<OrderItem> orderItems = orderDto.getOrderItems()
                .stream()
                .map(orderItemMapper::orderItemDtoToOrderItem)
                .collect(Collectors.toList());

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        Double price = orderItems
                .stream()
                .mapToDouble(orderItem -> orderItem.getQuantity() * orderItem.getItemPrice())
                .sum();

        order.setOrderItems(orderItems);
        order.setPrice(price);

        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections
                .singletonMap("message", "Order created!"));
    }

    public Map<Long, String> validateOrderAndCollectInvalidItems(OrderDto orderDto) {
        Map<Long, String> invalidOrderItems = new HashMap<>();

        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            Long foodId = orderItemDto.getFoodId();

            Food food = foodRepository.findById(foodId).orElse(null);

            if (food == null) {
                invalidOrderItems.put(foodId, "Food not found.");
            } else if (!food.isAvailable()) {
                invalidOrderItems.put(foodId, "Food not available.");
            }
        }

        return invalidOrderItems;
    }
}
