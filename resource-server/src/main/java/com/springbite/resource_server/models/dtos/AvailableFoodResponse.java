package com.springbite.resource_server.models.dtos;

import java.util.Date;
import java.util.Objects;

public class AvailableFoodResponse {

    private Long id;

    private String name;

    private Double price;

    private Double averageRating;

    private Date createdAt;

    private Boolean recommend;

    public AvailableFoodResponse() {
    }

    public AvailableFoodResponse(
            Long id,
            String name,
            Double price,
            Double averageRating,
            Date createdAt,
            Boolean recommend
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.averageRating = averageRating;
        this.createdAt = createdAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
        AvailableFoodResponse that = (AvailableFoodResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(averageRating, that.averageRating) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(recommend, that.recommend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, averageRating, createdAt, recommend);
    }

    @Override
    public String toString() {
        return "AvailableFoodResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", averageRating=" + averageRating +
                ", createdAt=" + createdAt +
                ", recommend=" + recommend +
                '}';
    }
}
