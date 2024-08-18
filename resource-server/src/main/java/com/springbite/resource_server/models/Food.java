package com.springbite.resource_server.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    private Double price;

    @Column(updatable = false)
    private Date createdAt;

    private Boolean recommended;

    @Enumerated(EnumType.STRING)
    private Cuisine cuisine;

    public Food() {
    }

    public Food(
            String name,
            String description,
            Double price,
            Boolean recommended,
            Cuisine cuisine
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = new Date();
        this.recommended = recommended;
        this.cuisine = cuisine;
    }

    public Food(
            String name,
            String description,
            Double averageRating,
            List<Rating> ratings,
            Double price,
            Boolean recommended,
            Cuisine cuisine
    ) {
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.ratings = ratings;
        this.price = price;
        this.createdAt = new Date();
        this.recommended = recommended;
        this.cuisine = cuisine;
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

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }
}
