package com.springbite.resource_server.models.dtos;

import java.util.Objects;

public class AvailableFoodResponse {

    private Long id;

    private String name;

    private String picture;

    private Double price;

    private Double averageRating;

    private boolean recommended;

    public AvailableFoodResponse() {
    }

    public AvailableFoodResponse(
            Long id,
            String name,
            String picture,
            Double price,
            Double averageRating,
            boolean recommended
    ) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.price = price;
        this.averageRating = averageRating;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
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
        AvailableFoodResponse that = (AvailableFoodResponse) o;
        return recommended == that.recommended &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(price, that.price) &&
                Objects.equals(averageRating, that.averageRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, picture, price, averageRating, recommended);
    }

    @Override
    public String toString() {
        return "AvailableFoodResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", averageRating=" + averageRating +
                ", recommended=" + recommended +
                '}';
    }
}
