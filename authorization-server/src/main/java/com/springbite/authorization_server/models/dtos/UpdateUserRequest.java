package com.springbite.authorization_server.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UpdateUserRequest {

    private final String emailRegex = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|" +
            "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

    @Email(regexp = emailRegex)
    private String username;

    @Size(min = 1, max = 50)
    private String firstname;

    @Size(min = 1, max = 50)
    private String lastname;

    @Size(min = 10, max = 15)
    private String phoneNumber;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String username, String firstname, String lastname, String phoneNumber) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }

    public @Email(regexp = emailRegex) String getUsername() {
        return username;
    }

    public void setUsername(@Email(regexp = emailRegex) String username) {
        this.username = username;
    }

    public @Size(min = 1, max = 50) String getFirstname() {
        return firstname;
    }

    public void setFirstname(@Size(min = 1, max = 50) String firstname) {
        this.firstname = firstname;
    }

    public @Size(min = 1, max = 50) String getLastname() {
        return lastname;
    }

    public void setLastname(@Size(min = 1, max = 50) String lastname) {
        this.lastname = lastname;
    }

    public @Size(min = 10, max = 15) String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Size(min = 10, max = 15) String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserRequest that = (UpdateUserRequest) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                username,
                firstname,
                lastname,
                phoneNumber);
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
