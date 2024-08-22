package com.springbite.resource_server.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class OrderItemDto {

    @NotNull
    private Long foodId;

    @NotNull
    @Size(min = 1)
    private Integer quantity;

    public OrderItemDto() {
    }

    public OrderItemDto(Long foodId, Integer quantity) {
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public @NotNull Long getFoodId() {
        return foodId;
    }

    public void setFoodId(@NotNull Long foodId) {
        this.foodId = foodId;
    }

    public @NotNull @Size(min = 1) Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull @Size(min = 1) Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemDto that = (OrderItemDto) o;
        return Objects.equals(foodId, that.foodId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodId, quantity);
    }

    @Override
    public String toString() {
        return "OrderItemDto{" +
                "foodId=" + foodId +
                ", quantity=" + quantity +
                '}';
    }
}
