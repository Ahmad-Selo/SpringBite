package com.springbite.authorization_server.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ChangePasswordRequestDTO {

    @NotBlank
    @Length(min = 8, max = 64)
    private String oldPassword;

    @NotBlank
    @Length(min = 8, max = 64)
    private String newPassword;

    public @NotBlank @Length(min = 8, max = 64) String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank @Length(min = 8, max = 64) String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank @Length(min = 8, max = 64) String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank @Length(min = 8, max = 64) String newPassword) {
        this.newPassword = newPassword;
    }
}
