package com.springbite.authorization_server.mappers;

import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.UserDto;
import com.springbite.authorization_server.models.dtos.UserPromoteResponse;
import com.springbite.authorization_server.models.dtos.UserResponseDto;
import org.springframework.stereotype.Component;

import static com.springbite.authorization_server.security.Role.ROLE_USER;

@Component
public class UserMapper {

    public User userDtoToUser(UserDto dto, boolean emailVerified) {
        return new User(
                dto.getUsername(),
                dto.getPassword(),
                dto.getFirstname(),
                dto.getLastname(),
                dto.getPhoneNumber(),
                dto.getPicture(),
                ROLE_USER,
                emailVerified,
                true,
                true
        );
    }

    public UserDto userToUserDto(User user) {
        return new UserDto(
                user.getUsername(),
                user.getPassword(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getPicture()
        );
    }

    public UserResponseDto userToUserResponseDto(User user) {
        return new UserResponseDto(
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

    public UserPromoteResponse userToUserPromoteResponse(User user) {
        return new UserPromoteResponse(user.getId(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getPicture(),
                user.getRole()
        );
    }
}
