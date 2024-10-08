package com.springbite.authorization_server.controllers;

import com.springbite.authorization_server.exceptions.UserNotFoundException;
import com.springbite.authorization_server.models.dtos.ChangePasswordRequest;
import com.springbite.authorization_server.models.dtos.DeleteUserRequest;
import com.springbite.authorization_server.models.dtos.PromoteRequest;
import com.springbite.authorization_server.models.dtos.UpdateUserRequest;
import com.springbite.authorization_server.security.HasRole;
import com.springbite.authorization_server.security.RequireOwnership;
import com.springbite.authorization_server.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user-id}")
    @RequireOwnership
    public ResponseEntity<?> getUser(
            @PathVariable("user-id") Long userId
    ) throws UserNotFoundException {
        return userService.getUser(userId);
    }

    @GetMapping("/search")
    @HasRole("ADMIN")
    public ResponseEntity<?> searchUser(
            @RequestParam(value = "username", required = false, defaultValue = "") String username,
            @RequestParam(value = "phone-number", required = false) String phoneNumber
    ) {
        return userService.searchUser(username, phoneNumber);
    }

    @PutMapping("/{user-id}/promote")
    @HasRole("ADMIN")
    public ResponseEntity<?> promoteUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody PromoteRequest promoteRequest
    ) throws UserNotFoundException {
        return userService.promoteUser(userId, promoteRequest);
    }

    @PutMapping("/{user-id}/change-password")
    @RequireOwnership
    public ResponseEntity<?> changePassword(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest
    ) throws UserNotFoundException {
        return userService.changePassword(userId, changePasswordRequest);
    }

    @PatchMapping("/{user-id}/update")
    @RequireOwnership
    public ResponseEntity<?> updateUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) throws UserNotFoundException {
        return userService.updateUser(userId, updateUserRequest);
    }

    @DeleteMapping("/{user-id}/delete")
    @RequireOwnership
    public ResponseEntity<?> deleteUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody DeleteUserRequest deleteUserRequest
    ) throws UserNotFoundException {
        return userService.deleteUser(userId, deleteUserRequest);
    }
}