package com.springbite.resource_server.models.dtos;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class OrderResponseDto {

    private Long id;

    private Date createdAt;

    private List<OrderItemResponseDto> orderItems;

    private String address;

    private Double price;

    public OrderResponseDto() {
    }

    public OrderResponseDto(Long id, Date createdAt, List<OrderItemResponseDto> orderItems, String address, Double price) {
        this.id = id;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
        this.address = address;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponseDto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponseDto> orderItems) {
        this.orderItems = orderItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponseDto that = (OrderResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(orderItems, that.orderItems) &&
                Objects.equals(address, that.address) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, orderItems, address, price);
    }

    @Override
    public String toString() {
        return "OrderResponseDto{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", orderItems=" + orderItems +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }
}
