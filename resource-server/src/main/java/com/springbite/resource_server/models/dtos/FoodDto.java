package com.springbite.resource_server.models.dtos;

import com.springbite.resource_server.models.Cuisine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FoodDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Boolean recommended;

    private Cuisine cuisine;

    public FoodDto() {
    }

    public FoodDto(
            String name,
            String description,
            Double price,
            Boolean recommended,
            Cuisine cuisine
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.recommended = recommended;
        this.cuisine = cuisine;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
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

    public @NotNull Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(@NotNull Boolean recommended) {
        this.recommended = recommended;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }
}
