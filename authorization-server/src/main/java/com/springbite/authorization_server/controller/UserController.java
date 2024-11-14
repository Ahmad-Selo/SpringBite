package com.springbite.authorization_server.controller;

import com.springbite.authorization_server.exception.UserNotFoundException;
import com.springbite.authorization_server.model.dto.ChangePasswordRequestDTO;
import com.springbite.authorization_server.model.dto.DeleteUserRequestDTO;
import com.springbite.authorization_server.model.dto.PromoteRequestDTO;
import com.springbite.authorization_server.model.dto.UpdateUserRequestDTO;
import com.springbite.authorization_server.security.authorization.HasRole;
import com.springbite.authorization_server.security.authorization.RequireOwnership;
import com.springbite.authorization_server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class UserController {

    private final Log logger = LogFactory.getLog(UserController.class);

    private final UserService userService;

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
            @Valid @RequestBody PromoteRequestDTO promoteRequestDTO
    ) throws UserNotFoundException {
        return userService.promoteUser(userId, promoteRequestDTO);
    }

    @PutMapping("/{user-id}/change-password")
    @RequireOwnership
    public ResponseEntity<?> changePassword(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO
    ) throws UserNotFoundException {
        return userService.changePassword(userId, changePasswordRequestDTO);
    }

    @PatchMapping("/{user-id}")
    @RequireOwnership
    public ResponseEntity<?> updateUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO
    ) throws UserNotFoundException {
        return userService.updateUser(userId, updateUserRequestDTO);
    }

    @DeleteMapping("/{user-id}")
    @RequireOwnership
    public ResponseEntity<?> deleteUser(
            @PathVariable("user-id") Long userId,
            @Valid @RequestBody DeleteUserRequestDTO deleteUserRequestDTO
    ) throws UserNotFoundException {
        return userService.deleteUser(userId, deleteUserRequestDTO);
    }
}