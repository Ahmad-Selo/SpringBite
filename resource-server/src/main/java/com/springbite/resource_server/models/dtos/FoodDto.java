package com.springbite.resource_server.models.dtos;

import com.springbite.resource_server.models.Cuisine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class FoodDto {

    @NotBlank
    private String name;

    @NotBlank
    private String picture;

    private String description;

    @NotNull
    private Double price;

    @NotNull
    private boolean available;

    @NotNull
    private boolean recommended;

    private Cuisine cuisine;

    public FoodDto() {
    }

    public FoodDto(
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

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotBlank String getPicture() {
        return picture;
    }

    public void setPicture(@NotBlank String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull Double getPrice() {
        return price;
    }

    public void setPrice(@NotNull Double price) {
        this.price = price;
    }

    @NotNull
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(@NotNull boolean available) {
        this.available = available;
    }

    @NotNull
    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(@NotNull boolean recommended) {
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
        FoodDto foodDto = (FoodDto) o;
        return available == foodDto.available &&
                recommended == foodDto.recommended &&
                Objects.equals(name, foodDto.name) &&
                Objects.equals(picture, foodDto.picture) &&
                Objects.equals(description, foodDto.description) &&
                Objects.equals(price, foodDto.price) &&
                cuisine == foodDto.cuisine;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, picture, description, price, available, recommended, cuisine);
    }

    @Override
    public String toString() {
        return "FoodDto{" +
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
