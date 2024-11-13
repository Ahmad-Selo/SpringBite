package com.springbite.authorization_server.mapper;

import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.model.SecurityUser;
import com.springbite.authorization_server.model.dto.UserRequestDTO;
import com.springbite.authorization_server.model.dto.UserPromoteResponseDTO;
import com.springbite.authorization_server.model.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

import static com.springbite.authorization_server.security.Role.ROLE_USER;

@Component
public class UserMapper {

    public User userDtoToUser(UserRequestDTO dto, boolean emailVerified, boolean phoneVerified) {
        return new User(
                dto.getUsername(),
                dto.getPassword(),
                dto.getFirstname(),
                dto.getLastname(),
                dto.getPhoneNumber(),
                dto.getPicture(),
                ROLE_USER,
                emailVerified,
                phoneVerified,
                true,
                true
        );
    }

    public UserRequestDTO userToUserDto(User user) {
        return new UserRequestDTO(
                user.getUsername(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getPicture()
        );
    }

    public UserResponseDTO userToUserResponseDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getPicture()
        );
    }

    public SecurityUser userToSecurityUser(User user) {
        return new SecurityUser(user);
    }

    public UserPromoteResponseDTO userToUserPromoteResponse(User user) {
        return new UserPromoteResponseDTO(user.getId(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getPicture(),
                user.getRole()
        );
    }
}
