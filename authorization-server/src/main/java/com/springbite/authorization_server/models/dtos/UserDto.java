package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class UserDto {

    @Email
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private String phoneNumber;

    public UserDto() {
    }

    public UserDto(String username, String password, String firstname, String lastname, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }

    public @Email @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@Email @NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public @NotBlank String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotBlank String firstname) {
        this.firstname = firstname;
    }

    public @NotBlank String getLastname() {
        return lastname;
    }

    public void setLastname(@NotBlank String lastname) {
        this.lastname = lastname;
    }

    public @NotBlank String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto dto = (UserDto) o;
        return Objects.equals(username, dto.username) &&
                Objects.equals(password, dto.password) &&
                Objects.equals(firstname, dto.firstname) &&
                Objects.equals(lastname, dto.lastname) &&
                Objects.equals(phoneNumber, dto.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, firstname, lastname, phoneNumber);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
