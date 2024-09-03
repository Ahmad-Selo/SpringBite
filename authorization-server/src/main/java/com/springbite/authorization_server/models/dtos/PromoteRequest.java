package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class PromoteRequest {

    @NotBlank
    private String role;

    public PromoteRequest() {
    }

    public PromoteRequest(String role) {
        this.role = role;
    }

    public @NotBlank String getRole() {
        return role;
    }

    public void setRole(@NotBlank String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromoteRequest that = (PromoteRequest) o;
        return Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }

    @Override
    public String toString() {
        return "PromoteRequest{" +
                "role='" + role + '\'' +
                '}';
    }
}
