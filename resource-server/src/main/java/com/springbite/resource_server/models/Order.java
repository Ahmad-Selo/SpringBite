package com.springbite.resource_server.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @Column(updatable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    private String address;

    private Double price;

    public Order() {
    }

    public Order(Long userId, String address) {
        this.userId = userId;
        this.address = address;
        this.createdAt = new Date();
    }

    public Order(List<OrderItem> orderItems, Double price, String address) {
        this.createdAt = new Date();
        this.orderItems = orderItems;
        this.price = price;
        this.address = address;
    }

    public Order(Long userId, List<OrderItem> orderItems, String address, Double price) {
        this.userId = userId;
        this.createdAt = new Date();
        this.orderItems = orderItems;
        this.address = address;
        this.price = price;
    }

    public Order(Long userId, Date createdAt, List<OrderItem> orderItems, String address, Double price) {
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
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
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(userId, order.userId) &&
                Objects.equals(createdAt, order.createdAt) &&
                Objects.equals(orderItems, order.orderItems) &&
                Objects.equals(address, order.address) &&
                Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, createdAt, orderItems, address, price);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", orderItems=" + orderItems +
                ", address='" + address + '\'' +
                ", price=" + price +
                '}';
    }
}
