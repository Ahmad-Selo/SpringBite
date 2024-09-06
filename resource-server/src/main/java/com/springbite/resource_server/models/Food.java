package com.springbite.resource_server.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "foods")
public class Food {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private String picture;

    private String description;

    private Double averageRating;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    private Long popularity;

    private Double price;

    @Column(updatable = false)
    private Date createdAt;

    private boolean available;

    private boolean recommended;

    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;

    @OneToMany(mappedBy = "food",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JsonManagedReference
    private List<OrderItem> orderItems;

    public Food() {
    }

    public Food(
            String name,
            String picture,
            String description,
            Double price,
            boolean available,
            boolean recommended,
            Cuisine cuisine
    ) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.createdAt = new Date();
        this.available = available;
        this.recommended = recommended;
        this.cuisine = cuisine;
    }

    public Food(
            String name,
            String picture,
            String description,
            Double averageRating,
            List<Rating> ratings,
            Long popularity,
            Double price,
            boolean available,
            boolean recommended,
            Cuisine cuisine
    ) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.averageRating = averageRating;
        this.ratings = ratings;
        this.popularity = popularity;
        this.price = price;
        this.createdAt = new Date();
        this.available = available;
        this.recommended = recommended;
        this.cuisine = cuisine;
    }

    public Food(
            String name,
            String picture,
            String description,
            Double averageRating,
            List<Rating> ratings,
            Long popularity,
            Double price,
            Date createdAt,
            boolean available,
            boolean recommended,
            Cuisine cuisine,
            List<OrderItem> orderItems
    ) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.averageRating = averageRating;
        this.ratings = ratings;
        this.popularity = popularity;
        this.price = price;
        this.createdAt = createdAt;
        this.available = available;
        this.recommended = recommended;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public Long getPopularity() {
        return popularity;
    }

    public void setPopularity(Long popularity) {
        this.popularity = popularity;
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return available == food.available &&
                recommended == food.recommended &&
                Objects.equals(id, food.id) &&
                Objects.equals(name, food.name) &&
                Objects.equals(picture, food.picture) &&
                Objects.equals(description, food.description) &&
                Objects.equals(averageRating, food.averageRating) &&
                Objects.equals(ratings, food.ratings) &&
                Objects.equals(popularity, food.popularity) &&
                Objects.equals(price, food.price) &&
                Objects.equals(createdAt, food.createdAt) &&
                cuisine == food.cuisine &&
                Objects.equals(orderItems, food.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                picture,
                description,
                averageRating,
                ratings,
                popularity,
                price,
                createdAt,
                available,
                recommended,
                cuisine,
                orderItems
        );
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", description='" + description + '\'' +
                ", averageRating=" + averageRating +
                ", ratings=" + ratings +
                ", popularity=" + popularity +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", available=" + available +
                ", recommended=" + recommended +
                ", cuisine=" + cuisine +
                ", orderItems=" + orderItems +
                '}';
    }
}
