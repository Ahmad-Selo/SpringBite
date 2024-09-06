package com.springbite.resource_server.models.dtos;

import java.util.Objects;

public class FoodResponseDto {

    private Long id;

    private String name;

    private String picture;

    private String description;

    private Double averageRating;

    private Long popularity;

    private Double price;

    private boolean recommended;

    public FoodResponseDto() {
    }

    public FoodResponseDto(
            Long id,
            String name,
            String picture,
            String description,
            Double averageRating,
            Long popularity,
            Double price,
            boolean recommended
    ) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.averageRating = averageRating;
        this.popularity = popularity;
        this.price = price;
        this.recommended = recommended;
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

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodResponseDto that = (FoodResponseDto) o;
        return recommended == that.recommended &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(description, that.description) &&
                Objects.equals(averageRating, that.averageRating) &&
                Objects.equals(popularity, that.popularity) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, picture, description, averageRating, popularity, price, recommended);
    }

    @Override
    public String toString() {
        return "FoodResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", description='" + description + '\'' +
                ", averageRating=" + averageRating +
                ", popularity=" + popularity +
                ", price=" + price +
                ", recommended=" + recommended +
                '}';
    }
}
