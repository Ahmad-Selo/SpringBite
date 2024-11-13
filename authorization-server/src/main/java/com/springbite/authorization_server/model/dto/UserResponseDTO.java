package com.springbite.authorization_server.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserResponseDTO {

    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private String phoneNumber;

    private String picture;
}
