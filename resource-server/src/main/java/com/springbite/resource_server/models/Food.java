package com.springbite.resource_server.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private Double averageRating;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    private Double price;

    @Column(updatable = false)
    private Date createdAt;

    private Boolean available;

    private Boolean recommend;

    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;

    @OneToMany(mappedBy = "food",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<OrderItem> orderItems;

    public Food() {
    }

    public Food(
            String name,
            String description,
            Double price,
            Boolean available,
            Boolean recommend,
            Cuisine cuisine
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = new Date();
        this.available = available;
        this.recommend = recommend;
        this.cuisine = cuisine;
    }

    public Food(
            String name,
            String description,
            Double averageRating,
            List<Rating> ratings,
            Double price,
            Boolean available,
            Boolean recommend,
            Cuisine cuisine
    ) {
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.ratings = ratings;
        this.price = price;
        this.createdAt = new Date();
        this.available = available;
        this.recommend = recommend;
        this.cuisine = cuisine;
    }

    public Food(
            String name,
            String description,
            Double averageRating,
            List<Rating> ratings,
            Double price,
            Date createdAt,
            Boolean available,
            Boolean recommend,
            Cuisine cuisine,
            Set<OrderItem> orderItems
    ) {
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.ratings = ratings;
        this.price = price;
        this.createdAt = createdAt;
        this.available = available;
        this.recommend = recommend;
        this.cuisine = cuisine;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(id, food.id) &&
                Objects.equals(name, food.name) &&
                Objects.equals(description, food.description) &&
                Objects.equals(averageRating, food.averageRating) &&
                Objects.equals(ratings, food.ratings) &&
                Objects.equals(price, food.price) &&
                Objects.equals(createdAt, food.createdAt) &&
                Objects.equals(available, food.available) &&
                Objects.equals(recommend, food.recommend) &&
                cuisine == food.cuisine &&
                Objects.equals(orderItems, food.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                description,
                averageRating,
                ratings,
                price,
                createdAt,
                available,
                recommend,
                cuisine,
                orderItems);
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", averageRating=" + averageRating +
                ", ratings=" + ratings +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", available=" + available +
                ", recommended=" + recommend +
                ", cuisine=" + cuisine +
                ", orderItems=" + orderItems +
                '}';
    }
}
