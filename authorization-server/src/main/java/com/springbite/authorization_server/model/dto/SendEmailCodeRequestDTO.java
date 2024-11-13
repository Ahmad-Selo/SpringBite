package com.springbite.authorization_server.model.dto;

import com.springbite.authorization_server.validation.constraint.Exists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SendEmailCodeRequestDTO {

    private static final String EMAIL_REGEX = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|" +
            "\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";

    @NotBlank
    @Email(regexp = EMAIL_REGEX)
    @Exists(table = "User", column = "username", message = "invalid username")
    private String username;

    public @NotBlank @Email(regexp = EMAIL_REGEX) @Exists(table = "User", column = "username", message = "invalid username") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Email(regexp = EMAIL_REGEX) @Exists(table = "User", column = "username", message = "invalid username") String username) {
        this.username = username;
    }
}
