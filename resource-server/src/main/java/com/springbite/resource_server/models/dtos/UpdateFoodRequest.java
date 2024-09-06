package com.springbite.resource_server.models.dtos;

import com.springbite.resource_server.models.Cuisine;

import java.util.Objects;

public class UpdateFoodRequest {

    private String name;

    private String picture;

    private String description;

    private Double price;

    private boolean available;

    private boolean recommended;

    private Cuisine cuisine;

    public UpdateFoodRequest() {
    }

    public UpdateFoodRequest(
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
        this.available = available;
        this.recommended = recommended;
        this.cuisine = cuisine;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateFoodRequest that = (UpdateFoodRequest) o;
        return available == that.available &&
                recommended == that.recommended &&
                Objects.equals(name, that.name) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(description, that.description) &&
                Objects.equals(price, that.price) &&
                cuisine == that.cuisine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, picture, description, price, available, recommended, cuisine);
    }

    @Override
    public String toString() {
        return "UpdateFoodRequest{" +
                "name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", recommended=" + recommended +
                ", cuisine=" + cuisine +
                '}';
    }
}
