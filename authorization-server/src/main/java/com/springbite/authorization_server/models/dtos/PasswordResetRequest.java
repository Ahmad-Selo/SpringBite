package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class PasswordResetRequest {

    @NotBlank
    @Size(min = 8)
    private String password;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(String password) {
        this.password = password;
    }

    public @NotBlank @Size(min = 8) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8) String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetRequest that = (PasswordResetRequest) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(password);
    }

    @Override
    public String toString() {
        return "RestPasswordRequest{" +
                "newPassword='" + password + '\'' +
                '}';
    }
}
