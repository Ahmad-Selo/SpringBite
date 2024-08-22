package com.springbite.resource_server.services;

import com.springbite.resource_server.models.dtos.OrderDto;
import com.springbite.resource_server.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<?> order(OrderDto orderDto) {
        return null;
    }
}
