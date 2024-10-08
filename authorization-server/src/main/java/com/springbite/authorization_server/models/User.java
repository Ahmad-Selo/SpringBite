package com.springbite.authorization_server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springbite.authorization_server.entities.ConfirmationCode;
import com.springbite.authorization_server.entities.PasswordResetCode;
import com.springbite.authorization_server.security.Role;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
    private List<ConfirmationCode> confirmationCodes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<PasswordResetCode> passwordResetCodes;

    private boolean emailVerified;

    private boolean nonLocked;

    private boolean enabled;

    public User() {
    }

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
        this.nonLocked = nonLocked;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ConfirmationCode> getConfirmationCodes() {
        return confirmationCodes;
    }

    public void setConfirmationCodes(List<ConfirmationCode> confirmationCodes) {
        this.confirmationCodes = confirmationCodes;
    }

    public List<PasswordResetCode> getPasswordResetCodes() {
        return passwordResetCodes;
    }

    public void setPasswordResetCodes(List<PasswordResetCode> passwordResetCodes) {
        this.passwordResetCodes = passwordResetCodes;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isNonLocked() {
        return nonLocked;
    }

    public void setNonLocked(boolean nonLocked) {
        this.nonLocked = nonLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return emailVerified == user.emailVerified &&
                nonLocked == user.nonLocked &&
                enabled == user.enabled &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                role == user.role &&
                Objects.equals(picture, user.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                username,
                password,
                firstname,
                lastname,
                phoneNumber,
                role,
                picture,
                emailVerified,
                nonLocked,
                enabled
        );
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", picture='" + picture + '\'' +
                ", emailVerified=" + emailVerified +
                ", nonLocked=" + nonLocked +
                ", enabled=" + enabled +
                '}';
    }
}
