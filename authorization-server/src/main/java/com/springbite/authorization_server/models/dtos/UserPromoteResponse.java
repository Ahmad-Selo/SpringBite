package com.springbite.authorization_server.models.dtos;

import com.springbite.authorization_server.security.Role;

import java.util.Objects;

public class UserPromoteResponse {

    private Long id;

    private String username;

    private String phoneNumber;

    private String picture;

    private Role role;

    public UserPromoteResponse() {
    }

    public UserPromoteResponse(Long id, String username, String phoneNumber, String picture, Role role) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.role = role;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPromoteResponse that = (UserPromoteResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(picture, that.picture) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, phoneNumber, picture, role);
    }

    @Override
    public String toString() {
        return "UserPromoteResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", picture='" + picture + '\'' +
                ", role=" + role +
                '}';
    }
}
