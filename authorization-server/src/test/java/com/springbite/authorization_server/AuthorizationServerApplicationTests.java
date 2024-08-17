package com.springbite.authorization_server;

import com.springbite.authorization_server.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorizationServerApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Test
    void AuthenticatingWithValidUser() throws Exception {
        mvc.perform(formLogin()
                .user("ahmad_selo")
                .password("password")
        ).andExpect(authenticated());
    }

    @Test
    void AuthenticatingWithInvalidUser() throws Exception {
        mvc.perform(formLogin()
                .user("john_doe")
                .password("password")
        ).andExpect(unauthenticated());
    }
}
