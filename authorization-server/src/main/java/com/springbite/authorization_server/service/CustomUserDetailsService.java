package com.springbite.authorization_server.service;

import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.exception.UserNotFoundException;
import com.springbite.authorization_server.model.SecurityUser;
import com.springbite.authorization_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrPhoneNumber(username, username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        return new SecurityUser(user);
    }
}
