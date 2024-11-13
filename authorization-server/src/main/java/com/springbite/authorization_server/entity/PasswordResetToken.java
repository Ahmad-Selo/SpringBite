package com.springbite.authorization_server.entity;

import com.springbite.authorization_server.exception.TokenExpiredException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String token;

    private Date createdAt;

    private Date expiresAt;

    private boolean expired;

    @OneToOne
    private PasswordResetCode passwordResetCode;

    public PasswordResetToken(PasswordResetCode passwordResetCode) {
        this.token = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.expiresAt = new Date(createdAt.getTime() + 900000);
        this.expired = false;
        this.passwordResetCode = passwordResetCode;
    }

    public void validateToken() throws TokenExpiredException {
        if (System.currentTimeMillis() >= this.expiresAt.getTime() || expired) {
            throw new TokenExpiredException("The token you provided has expired. Please request a new code");
        }
    }
}
