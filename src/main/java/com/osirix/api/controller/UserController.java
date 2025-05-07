package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;
import com.osirix.api.service.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private static final String USER_RESOURCE = "/users";
    private static final String USER_ID_PATH = USER_RESOURCE + "/{userId}";
    private static final String NORMAL_USERS_PATH = USER_RESOURCE + "/normal";
    private static final String USER_TOGGLE_ENABLE_PATH = USER_ID_PATH + "/toggle-enable";
    private static final String USER_TOGGLE_LOCK_PATH = USER_ID_PATH + "/toggle-lock";


    @Autowired
    UserServiceImpl userService;

    @GetMapping(value = USER_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong users....");
    }

    @GetMapping(USER_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAll();
        ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>(
                "All users fetched successfully",
                HttpStatus.OK.value(),
                users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(NORMAL_USERS_PATH)
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getNormalUsers() {
        List<UserResponseDto> users = userService.getNormalUsers();
        ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>(
                "Normal users fetched successfully",
                HttpStatus.OK.value(),
                users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(USER_ID_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User fetched successfully",
                HttpStatus.OK.value(),
                user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(USER_ID_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(userId, userRequestDto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User updated successfully",
                HttpStatus.OK.value(),
                updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(USER_ID_PATH)
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(USER_TOGGLE_ENABLE_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> toggleUserEnable(@PathVariable Long userId) {
        UserResponseDto updatedUser = userService.toggleEnable(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User enable status toggled successfully",
                HttpStatus.OK.value(),
                updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(USER_TOGGLE_LOCK_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> toggleUserLockAccount(@PathVariable Long userId) {
        UserResponseDto updatedUser = userService.toggleLockAccount(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User account lock status toggled successfully",
                HttpStatus.OK.value(),
                updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}