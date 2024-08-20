package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class SignupWithProviderRequest {

    @NotBlank
    @Size(min = 10, max = 15)
    private String phoneNumber;

    public SignupWithProviderRequest() {
    }

    public SignupWithProviderRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank @Size(min = 10, max = 15) String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank @Size(min = 10, max = 15) String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignupWithProviderRequest that = (SignupWithProviderRequest) o;
        return Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(phoneNumber);
    }

    @Override
    public String toString() {
        return "SignupWithProviderRequest{" +
                "phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
