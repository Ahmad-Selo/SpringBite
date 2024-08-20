package com.springbite.authorization_server.mappers;

import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.UserDto;
import org.springframework.stereotype.Component;

import static com.springbite.authorization_server.security.Role.ROLE_USER;

@Component
public class UserMapper {

    public User userDtoToUser(UserDto dto) {
        return new User(
                dto.getUsername(),
                dto.getPassword(),
                dto.getFirstname(),
                dto.getLastname(),
                dto.getPhoneNumber(),
                ROLE_USER,
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
                user.getPhoneNumber()
        );
    }

    public SecurityUser userToSecurityUser(User user) {
        return new SecurityUser(user);
    }
}
