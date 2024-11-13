package com.springbite.authorization_server.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PasswordResetRequestDTO {

    @NotBlank
    @Length(min = 8, max = 64)
    private String password;

    public @NotBlank @Length(min = 8, max = 64) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Length(min = 8, max = 64) String password) {
        this.password = password;
    }
}
