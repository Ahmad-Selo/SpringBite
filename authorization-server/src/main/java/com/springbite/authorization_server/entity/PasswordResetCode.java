package com.springbite.authorization_server.entity;

import com.springbite.authorization_server.exception.CodeExpiredException;
import com.springbite.authorization_server.security.RandomGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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

    public PasswordResetCode(User user) {
        this.code = RandomGenerator.code(6);
        this.createdAt = new Date();
        this.expiresAt = new Date(createdAt.getTime() + 900000);
        this.expired = false;
        this.user = user;
    }

    public void validateCode() throws CodeExpiredException {
        if (System.currentTimeMillis() >= expiresAt.getTime() || expired) {
            throw new CodeExpiredException("The code you provided has expired. Please request a new code.");
        }
    }
}
