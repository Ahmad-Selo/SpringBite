package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UserDto {

    private final String emailRegex = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|" +
            "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

    @Email(regexp = emailRegex)
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 64)
    private String password;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstname;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastname;

    @NotBlank
    @Size(min = 10, max = 15)
    private String phoneNumber;

    @NotBlank
    private String picture;

    public UserDto() {
    }

    public UserDto(String username,
                   String password,
                   String firstname,
                   String lastname,
                   String phoneNumber,
                   String picture
    ) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
    }

    public @Email(regexp = emailRegex) @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@Email(regexp = emailRegex) @NotBlank String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 8, max = 64) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 8, max = 64) String password) {
        this.password = password;
    }

    public @NotBlank @Size(min = 1, max = 50) String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotBlank @Size(min = 1, max = 50) String firstname) {
        this.firstname = firstname;
    }

    public @NotBlank @Size(min = 1, max = 50) String getLastname() {
        return lastname;
    }

    public void setLastname(@NotBlank @Size(min = 1, max = 50) String lastname) {
        this.lastname = lastname;
    }

    public @NotBlank @Size(min = 10, max = 15) String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank @Size(min = 10, max = 15) String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank String getPicture() {
        return picture;
    }

    public void setPicture(@NotBlank String picture) {
        this.picture = picture;
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
                Objects.equals(phoneNumber, dto.phoneNumber) &&
                Objects.equals(picture, dto.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, firstname, lastname, phoneNumber, picture);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "emailRegex='" + emailRegex + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imagePath='" + picture + '\'' +
                '}';
    }
}
