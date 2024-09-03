package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class ChangePasswordRequest {

    @NotBlank
    @Size(min = 8, max = 64)
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 64)
    private String newPassword;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public @NotBlank @Size(min = 8, max = 64) String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank @Size(min = 8, max = 64) String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank @Size(min = 8, max = 64) String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank @Size(min = 8, max = 64) String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChangePasswordRequest that = (ChangePasswordRequest) o;
        return Objects.equals(oldPassword, that.oldPassword) && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldPassword, newPassword);
    }

    @Override
    public String toString() {
        return "ChangePasswordRequest{" +
                "oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
