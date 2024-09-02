package com.springbite.authorization_server.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "key_pairs")
public class KeyPairEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String kid;

    @Lob
    private String publicKey;

    @Lob
    private String privateKey;

    public KeyPairEntity() {
    }

    public KeyPairEntity(String publicKey, String privateKey) {
        this.kid = UUID.randomUUID().toString();
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public KeyPairEntity(String kid, String publicKey, String privateKey) {
        this.kid = kid;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyPairEntity that = (KeyPairEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(kid, that.kid) &&
                Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(privateKey, that.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kid, publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "KeyPairEntity{" +
                "id=" + id +
                ", kid='" + kid + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}
