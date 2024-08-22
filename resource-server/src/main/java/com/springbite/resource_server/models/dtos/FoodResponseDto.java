package com.springbite.resource_server.models.dtos;

import java.util.Objects;

public class FoodResponseDto {

    private Long id;

    private String name;

    private String description;

    private Double averageRating;

    private Double price;

    private Boolean recommend;

    public FoodResponseDto() {
    }

    public FoodResponseDto(
            Long id,
            String name,
            String description,
            Double averageRating,
            Double price,
            Boolean recommend
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.price = price;
        this.recommend = recommend;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodResponseDto that = (FoodResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(averageRating, that.averageRating) &&
                Objects.equals(price, that.price) &&
                Objects.equals(recommend, that.recommend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, averageRating, price, recommend);
    }

    @Override
    public String toString() {
        return "FoodResponseDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", averageRating=" + averageRating +
                ", price=" + price +
                ", recommend=" + recommend +
                '}';
    }
}
