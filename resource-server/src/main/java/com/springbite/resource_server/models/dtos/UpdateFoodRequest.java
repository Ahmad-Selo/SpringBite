package com.springbite.resource_server.models.dtos;

import com.springbite.resource_server.models.Cuisine;

import java.util.Objects;

public class UpdateFoodRequest {

    private String name;

    private String description;

    private Double price;

    private Boolean available;

    private Boolean recommend;

    private Cuisine cuisine;

    public UpdateFoodRequest() {
    }

    public UpdateFoodRequest(
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
        this.available = available;
        this.recommend = recommend;
        this.cuisine = cuisine;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateFoodRequest that = (UpdateFoodRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                Objects.equals(available, that.available) &&
                Objects.equals(recommend, that.recommend) &&
                cuisine == that.cuisine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, available, recommend, cuisine);
    }
}
