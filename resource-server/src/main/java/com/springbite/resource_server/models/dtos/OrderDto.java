package com.springbite.resource_server.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.Set;

public class OrderDto {

    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 1)
    private Set<OrderItemDto> orderItems;

    @NotBlank
    private String address;

    public OrderDto() {
    }

    public OrderDto(Long userId, Set<OrderItemDto> orderItems, String address) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.address = address;
    }

    public @NotNull Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Long userId) {
        this.userId = userId;
    }

    public @NotNull @Size(min = 1) Set<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(@NotNull @Size(min = 1) Set<OrderItemDto> orderItems) {
        this.orderItems = orderItems;
    }

    public @NotBlank String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(userId, orderDto.userId) &&
                Objects.equals(orderItems, orderDto.orderItems) &&
                Objects.equals(address, orderDto.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, orderItems, address);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "userId=" + userId +
                ", orderItems=" + orderItems +
                ", address='" + address + '\'' +
                '}';
    }
}
