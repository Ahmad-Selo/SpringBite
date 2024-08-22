package com.springbite.resource_server.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Food food;

    private Long userId;

    private Double ratingValue;

    @Column(updatable = false)
    private Date createdAt;

    public Rating() {
    }

    public Rating(Food food, Long userId, Double ratingValue) {
        this.food = food;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.createdAt = new Date();
    }

    public Rating(Food food, Long userId, Double ratingValue, Date createdAt) {
        this.food = food;
        this.userId = userId;
        this.ratingValue = ratingValue;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


}
