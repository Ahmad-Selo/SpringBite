package com.springbite.authorization_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springbite.authorization_server.security.Role;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String phoneNumber;

    private String picture;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<VerificationCode> verificationCodes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<PasswordResetCode> passwordResetCodes;

    private boolean phoneVerified;

    private boolean emailVerified;

    private boolean nonLocked;

    private boolean enabled;

    public User(String username, String password, String firstname, String lastname, String phoneNumber, String picture) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
    }

    public User(
            String username,
            String password,
            String firstname,
            String lastname,
            String phoneNumber,
            String picture,
            Role role,
            boolean emailVerified,
            boolean phoneVerified,
            boolean nonLocked,
            boolean enabled
    ) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.role = role;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
        this.nonLocked = nonLocked;
        this.enabled = enabled;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }
}