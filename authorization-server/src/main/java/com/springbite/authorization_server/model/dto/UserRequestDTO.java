package com.springbite.authorization_server.model.dto;

import com.springbite.authorization_server.validation.constraint.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class UserRequestDTO {

    private static final String EMAIL_REGEX = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|" +
            "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

    @Email(regexp = EMAIL_REGEX)
    @NotBlank
    @Unique(table = "User", column = "username", message = "invalid username")
    private String username;

    @NotBlank
    @Length(min = 8, max = 64)
    private String password;

    @NotBlank
    @Length(min = 1, max = 50)
    private String firstname;

    @NotBlank
    @Length(min = 1, max = 50)
    private String lastname;

    @NotBlank
    @Length(min = 10, max = 10)
    @Unique(table = "User", column = "phoneNumber", message = "invalid phone number")
    private String phoneNumber;

    @NotBlank
    private String picture;

    public UserRequestDTO(String username,
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

    public @Email(regexp = EMAIL_REGEX) @NotBlank @Unique(table = "User", column = "username", message = "invalid username") String getUsername() {
        return username;
    }

    public void setUsername(@Email(regexp = EMAIL_REGEX) @NotBlank @Unique(table = "User", column = "username", message = "invalid username") String username) {
        this.username = username;
    }

    public @NotBlank @Length(min = 8, max = 64) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Length(min = 8, max = 64) String password) {
        this.password = password;
    }

    public @NotBlank @Length(min = 1, max = 50) String getFirstname() {
        return firstname;
    }

    public void setFirstname(@NotBlank @Length(min = 1, max = 50) String firstname) {
        this.firstname = firstname;
    }

    public @NotBlank @Length(min = 1, max = 50) String getLastname() {
        return lastname;
    }

    public void setLastname(@NotBlank @Length(min = 1, max = 50) String lastname) {
        this.lastname = lastname;
    }

    public @NotBlank @Length(min = 10, max = 10) @Unique(table = "User", column = "phoneNumber", message = "invalid phone number") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank @Length(min = 10, max = 10) @Unique(table = "User", column = "phoneNumber", message = "invalid phone number") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank String getPicture() {
        return picture;
    }

    public void setPicture(@NotBlank String picture) {
        this.picture = picture;
    }
}
