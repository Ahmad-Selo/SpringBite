package com.springbite.authorization_server.entities;

import com.springbite.authorization_server.exceptions.CodeExpiredException;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.security.CodeGenerator;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "password_reset_codes")
public class PasswordResetCode {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String code;

    private Date createdAt;

    private Date expiresAt;

    private boolean expired;

    @ManyToOne
    private User user;

    @OneToOne(mappedBy = "passwordResetCode", cascade = CascadeType.REMOVE)
    private PasswordResetToken passwordResetToken;

    public PasswordResetCode() {
    }

    public PasswordResetCode(User user) {
        this.code = CodeGenerator.generateCode();
        this.createdAt = new Date();
        this.expiresAt = new Date(createdAt.getTime() + 900000);
        this.expired = false;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PasswordResetToken getPasswordRestToken() {
        return passwordResetToken;
    }

    public void setPasswordRestToken(PasswordResetToken passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public void validateCode() throws CodeExpiredException {
        if (System.currentTimeMillis() >= expiresAt.getTime() || expired) {
            throw new CodeExpiredException("The code you provided has expired. Please request a new code.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetCode that = (PasswordResetCode) o;
        return expired == that.expired &&
                Objects.equals(id, that.id) &&
                Objects.equals(code, that.code) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, createdAt, expiresAt, expired, user);
    }

    @Override
    public String toString() {
        return "PasswordRestCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", used=" + expired +
                ", user=" + user +
                '}';
    }
}
