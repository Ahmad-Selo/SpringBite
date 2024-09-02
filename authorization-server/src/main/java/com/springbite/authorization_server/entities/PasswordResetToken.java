package com.springbite.authorization_server.entities;

import com.springbite.authorization_server.exceptions.TokenExpiredException;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

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

    @OneToOne
    private PasswordResetCode passwordResetCode;

    public PasswordResetToken() {
    }

    public PasswordResetToken(PasswordResetCode passwordResetCode) {
        this.token = UUID.randomUUID().toString();
        this.createdAt = new Date();
        this.expiresAt = new Date(createdAt.getTime() + 900000);
        this.passwordResetCode = passwordResetCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public PasswordResetCode getPasswordRestCode() {
        return passwordResetCode;
    }

    public void setPasswordRestCode(PasswordResetCode passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public void validateToken() throws TokenExpiredException {
        if (System.currentTimeMillis() >= this.expiresAt.getTime()) {
            throw new TokenExpiredException("The token you provided has expired. Please request a new code.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetToken that = (PasswordResetToken) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(token, that.token) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(passwordResetCode, that.passwordResetCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, createdAt, expiresAt, passwordResetCode);
    }

    @Override
    public String toString() {
        return "PasswordRestToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", passwordRestCode=" + passwordResetCode +
                '}';
    }
}
