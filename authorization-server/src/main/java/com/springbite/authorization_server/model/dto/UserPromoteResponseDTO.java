package com.springbite.authorization_server.model.dto;

import com.springbite.authorization_server.security.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserPromoteResponseDTO {

    private Long id;

    private String username;

    private String phoneNumber;

    private String picture;

    private Role role;
}
