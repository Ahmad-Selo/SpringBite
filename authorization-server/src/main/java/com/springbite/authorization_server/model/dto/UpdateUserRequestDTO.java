package com.springbite.authorization_server.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UpdateUserRequestDTO {

    private static final String EMAIL_REGEX = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|" +
            "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

    @Email(regexp = EMAIL_REGEX)
    private String username;

    @Length(min = 1, max = 50)
    private String firstname;

    @Length(min = 1, max = 50)
    private String lastname;

    @Length(min = 10, max = 10)
    private String phoneNumber;

    private String picture;

    public @Email(regexp = EMAIL_REGEX) String getUsername() {
        return username;
    }

    public void setUsername(@Email(regexp = EMAIL_REGEX) String username) {
        this.username = username;
    }

    public @Length(min = 1, max = 50) String getFirstname() {
        return firstname;
    }

    public void setFirstname(@Length(min = 1, max = 50) String firstname) {
        this.firstname = firstname;
    }

    public @Length(min = 1, max = 50) String getLastname() {
        return lastname;
    }

    public void setLastname(@Length(min = 1, max = 50) String lastname) {
        this.lastname = lastname;
    }

    public @Length(min = 10, max = 10) String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Length(min = 10, max = 10) String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
