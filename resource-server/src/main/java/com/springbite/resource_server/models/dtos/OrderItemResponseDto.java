package com.springbite.resource_server.models.dtos;

import java.util.Objects;

public class OrderItemResponseDto {

    private String foodName;

    private Double itemPrice;

    private Integer quantity;

    public OrderItemResponseDto() {
    }

    public OrderItemResponseDto(String foodName, Double itemPrice, Integer quantity) {
        this.foodName = foodName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemResponseDto that = (OrderItemResponseDto) o;
        return Objects.equals(foodName, that.foodName) &&
                Objects.equals(itemPrice, that.itemPrice) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodName, itemPrice, quantity);
    }

    @Override
    public String toString() {
        return "ItemOrderResponseDto{" +
                "foodName='" + foodName + '\'' +
                ", itemPrice=" + itemPrice +
                ", quantity=" + quantity +
                '}';
    }
}
