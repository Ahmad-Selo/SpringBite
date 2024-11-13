package com.springbite.authorization_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "key_pairs")
public class KeyPairEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String kid;

    @Lob
    private String publicKey;

    @Lob
    private String privateKey;

    public KeyPairEntity(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
