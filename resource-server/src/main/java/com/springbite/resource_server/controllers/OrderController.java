package com.springbite.resource_server.controllers;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.models.dtos.OrderDto;
import com.springbite.resource_server.security.CustomAuthentication;
import com.springbite.resource_server.security.HasAnyRole;
import com.springbite.resource_server.security.RequireOrderOwnershipOrHasAnyRole;
import com.springbite.resource_server.security.RequireOwnership;
import com.springbite.resource_server.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    @HasAnyRole(roles = {"'ADMIN'", "'MANAGER'"})
    public ResponseEntity<?> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/users/{user-id}")
    @RequireOwnership
    public ResponseEntity<?> getOrders(
            @PathVariable("user-id") Long userId
    ) {
        return orderService.getOrders(userId);
    }

    @GetMapping("/{order-id}")
    @RequireOrderOwnershipOrHasAnyRole(roles = {"'ADMIN'", "'MANAGER'"})
    public ResponseEntity<?> getOrder(
            @PathVariable("order-id") Long orderId
    ) throws ResourceNotFoundException {
        return orderService.getOrder(orderId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDto orderDto,
            CustomAuthentication customAuthentication
    ) {
        return orderService.createOrder(orderDto, customAuthentication.getUserId());
    }
}
