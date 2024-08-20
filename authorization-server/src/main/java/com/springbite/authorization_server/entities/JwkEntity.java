package com.springbite.authorization_server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "jwks")
public class JwkEntity {

    @Id
    private String kid;

    @Lob
    private String publicKey;

    public JwkEntity() {
    }

    public JwkEntity(String kid, String publicKey) {
        this.kid = kid;
        this.publicKey = publicKey;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwkEntity jwkEntity = (JwkEntity) o;
        return Objects.equals(kid, jwkEntity.kid) && Objects.equals(publicKey, jwkEntity.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kid, publicKey);
    }

    @Override
    public String toString() {
        return "JwkEntity{" +
                "kid='" + kid + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
