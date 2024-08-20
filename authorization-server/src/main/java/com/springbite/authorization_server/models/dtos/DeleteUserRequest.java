package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class DeleteUserRequest {

    @NotNull
    @Size(min = 8)
    private String password;

    public DeleteUserRequest() {
    }

    public DeleteUserRequest(String password) {
        this.password = password;
    }

    public @NotNull @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotNull @Size(min = 8) String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteUserRequest that = (DeleteUserRequest) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }

    @Override
    public String toString() {
        return "DeleteUserRequest{" +
                "password='" + password + '\'' +
                '}';
    }
}
