package com.springbite.resource_server.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

public class OrderDto {

    @NotNull
    @Size(min = 1)
    private List<OrderItemDto> orderItems;

    @NotBlank
    private String address;

    public OrderDto() {
    }

    public OrderDto(List<OrderItemDto> orderItems, String address) {
        this.orderItems = orderItems;
        this.address = address;
    }

    public @NotNull @Size(min = 1) List<OrderItemDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(@NotNull @Size(min = 1) List<OrderItemDto> orderItems) {
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
        return Objects.equals(orderItems, orderDto.orderItems) && Objects.equals(address, orderDto.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItems, address);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderItems=" + orderItems +
                ", address='" + address + '\'' +
                '}';
    }
}
