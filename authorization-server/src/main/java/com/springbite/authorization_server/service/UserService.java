package com.springbite.authorization_server.service;

import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.exception.UserNotFoundException;
import com.springbite.authorization_server.mapper.UserMapper;
import com.springbite.authorization_server.model.dto.*;
import com.springbite.authorization_server.repository.UserRepository;
import com.springbite.authorization_server.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> getUser(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

        UserResponseDTO userResponseDto = userMapper.userToUserResponseDto(user);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    public ResponseEntity<?> searchUser(String username, String phoneNumber) {
        List<UserPromoteResponseDTO> users;

        if (phoneNumber != null) {
            users = userRepository.findByPhoneNumberContaining(phoneNumber)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(userMapper::userToUserPromoteResponse)
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findByUsernameContaining(username)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(userMapper::userToUserPromoteResponse)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.status(HttpStatus.OK).body(users);

    }

    public ResponseEntity<?> promoteUser(Long userId, PromoteRequestDTO promoteRequestDTO) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

        Role role = Role.valueOf("ROLE_" + promoteRequestDTO.getRole());

        user.setRole(role);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "The user has been successfully promoted to the new role."));
    }

    public ResponseEntity<?> changePassword(Long userId, ChangePasswordRequestDTO changePasswordRequestDTO)
            throws UserNotFoundException {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Password incorrect."));
        }

        if (passwordEncoder.matches(changePasswordRequestDTO.getNewPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections
                    .singletonMap("message", "Your new password cannot be the same as your current password. Please choose a different password to ensure the security of your account."));
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "Password have been changed successfully."));
    }

    public ResponseEntity<?> updateUser(Long userId, UpdateUserRequestDTO updateUserRequestDTO)
            throws UserNotFoundException {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

        String username = updateUserRequestDTO.getUsername();
        String firstname = updateUserRequestDTO.getFirstname();
        String lastname = updateUserRequestDTO.getLastname();
        String phoneNumber = updateUserRequestDTO.getPhoneNumber();

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

        UserResponseDTO userResponseDto = userMapper.userToUserResponseDto(savedUser);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    public ResponseEntity<?> deleteUser(Long userId, DeleteUserRequestDTO deleteUserRequestDTO)
            throws UserNotFoundException {
        User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!passwordEncoder.matches(deleteUserRequestDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections
                    .singletonMap("error", "Password incorrect."));
        }

        userRepository.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body(Collections
                .singletonMap("message", "User have been deleted successfully."));
    }

}
