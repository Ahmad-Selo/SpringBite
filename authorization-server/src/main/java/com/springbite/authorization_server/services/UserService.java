package com.springbite.authorization_server.services;

import com.springbite.authorization_server.exceptions.UserNotFoundException;
import com.springbite.authorization_server.mappers.UserMapper;
import com.springbite.authorization_server.models.User;
import com.springbite.authorization_server.models.dtos.ChangePasswordRequest;
import com.springbite.authorization_server.models.dtos.DeleteUserRequest;
import com.springbite.authorization_server.models.dtos.UpdateUserRequest;
import com.springbite.authorization_server.models.dtos.UserResponseDto;
import com.springbite.authorization_server.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> getUser(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

            UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

            return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }
    }

    public ResponseEntity<?> changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Password incorrect."));
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Password have been changed successfully."));
    }

    public ResponseEntity<?> updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        String username = updateUserRequest.getUsername();
        String firstname = updateUserRequest.getFirstname();
        String lastname = updateUserRequest.getLastname();
        String phoneNumber = updateUserRequest.getPhoneNumber();

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        if (firstname != null && !firstname.isBlank()) {
            user.setFirstname(firstname);
        }

        if (lastname != null && !lastname.isBlank()) {
            user.setLastname(lastname);
        }

        if (phoneNumber != null && !phoneNumber.isBlank()) {
            user.setPhoneNumber(phoneNumber);
        }

        User savedUser = userRepository.save(user);

        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(savedUser);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    public ResponseEntity<?> deleteUser(Long userId, DeleteUserRequest deleteUserRequest) {
        User user;

        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("error", e.getMessage()));
        }

        if (!passwordEncoder.matches(deleteUserRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Password incorrect."));
        }

        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "User have been deleted successfully."));
    }

}
