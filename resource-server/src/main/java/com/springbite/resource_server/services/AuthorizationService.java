package com.springbite.resource_server.services;

import com.springbite.resource_server.exceptions.ResourceNotFoundException;
import com.springbite.resource_server.models.Order;
import com.springbite.resource_server.repositories.OrderRepository;
import com.springbite.resource_server.security.CustomAuthentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthorizationService {

    private final OrderRepository orderRepository;

    public AuthorizationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean hasOrderOwnership(
            CustomAuthentication customAuthentication,
            Long orderId
    ) throws ResourceNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        return Objects.equals(order.getUserId(), customAuthentication.getUserId());
    }

    public boolean hasAnyRole(
            CustomAuthentication customAuthentication,
            String[] roles
    ) {
        return customAuthentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> {
                    for (String role : roles) {
                        if (grantedAuthority.getAuthority().equals("ROLE_" + role)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    public boolean hasOrderOwnershipOrHasAnyRole(
            CustomAuthentication customAuthentication,
            Long orderId,
            String... roles
    ) throws ResourceNotFoundException {
        return hasOrderOwnership(customAuthentication, orderId) || hasAnyRole(customAuthentication, roles);
    }
}
