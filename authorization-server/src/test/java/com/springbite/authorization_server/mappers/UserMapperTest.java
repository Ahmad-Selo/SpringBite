package com.springbite.authorization_server.mappers;

import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.springbite.authorization_server.security.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void userDtoToUserTest() {
        UserDto dto = new UserDto(
                "ahmad_selo",
                "password",
                "Ahmad",
                "Selo",
                "0981227881"
        );

        User user = userMapper.userDtoToUser(dto);

        assertEquals(dto.getUsername(), user.getUsername());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.getFirstname(), user.getFirstname());
        assertEquals(dto.getLastname(), user.getLastname());
        assertEquals(dto.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    void userToUserSecurityTest() {
        User user = new User(
                "ahmad_selo",
                "password",
                "Ahmad",
                "Selo",
                "0981227881",
                ROLE_USER,
                true,
                true
        );

        SecurityUser securityUser = userMapper.userToSecurityUser(user);

        assertEquals(user.getUsername(), securityUser.getUsername());

        assertEquals(user.getPassword(), securityUser.getPassword());

        assertEquals(user.getFirstname(), securityUser.getUser().getFirstname());

        assertEquals(user.getLastname(), securityUser.getUser().getLastname());

        assertEquals(user.getPhoneNumber(), securityUser.getUser().getPhoneNumber());

        assertEquals(user.getRole().toString(), securityUser.getAuthorities()
                .stream()
                .findAny()
                .orElseThrow(() -> new NullPointerException())
                .getAuthority());

        assertEquals(user.isNonLocked(), securityUser.getUser().isNonLocked());

        assertEquals(user.isEnabled(), securityUser.getUser().isEnabled());

    }
}