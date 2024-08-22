package com.springbite.resource_server.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Food food;

    private Double itemPrice;

    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(Order order, Food food, Double itemPrice, Integer quantity) {
        this.order = order;
        this.food = food;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
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
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) &&
                Objects.equals(order, orderItem.order) &&
                Objects.equals(food, orderItem.food) &&
                Objects.equals(itemPrice, orderItem.itemPrice) &&
                Objects.equals(quantity, orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, food, itemPrice, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
                ", food=" + food +
                ", itemPrice=" + itemPrice +
                ", quantity=" + quantity +
                '}';
    }
}
