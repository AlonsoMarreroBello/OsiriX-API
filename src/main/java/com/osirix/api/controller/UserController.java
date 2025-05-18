package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;
import com.osirix.api.service.impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private static final String USER_RESOURCE = "/users";
    private static final String USER_ID_PATH = USER_RESOURCE + "/{userId}";
    private static final String NORMAL_USERS_PATH = USER_RESOURCE + "/normal";
    private static final String USER_TOGGLE_ENABLE_PATH = USER_ID_PATH + "/toggle-enable";
    private static final String USER_TOGGLE_LOCK_PATH = USER_ID_PATH + "/toggle-lock";


    @Autowired
    UserServiceImpl userService;

    @Operation(summary = "Ping User Controller", description = "A simple ping endpoint to check if the User controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = USER_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong users....");
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(USER_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> users = userService.getAll();
        ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>(
                "All users fetched successfully",
                HttpStatus.OK.value(),
                users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get normal users", description = "Retrieves a list of users who are not staff, publishers, or developers (i.e., standard users).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved normal users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(NORMAL_USERS_PATH)
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getNormalUsers() {
        List<UserResponseDto> users = userService.getNormalUsers();
        ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>(
                "Normal users fetched successfully",
                HttpStatus.OK.value(),
                users);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(USER_ID_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true, example = "1") @PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User fetched successfully",
                HttpStatus.OK.value(),
                user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Update an existing user", description = "Updates an existing user's details. Note: For registration, use the /auth/register endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for user update (e.g., invalid email format, username too short)"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID to update"),
            @ApiResponse(responseCode = "409", description = "Conflict - Updated username or email might already be in use by another user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(USER_ID_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(
            @Parameter(description = "ID of the user to update", required = true, example = "1") @PathVariable Long userId,
            @Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(userId, userRequestDto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User updated successfully",
                HttpStatus.OK.value(),
                updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(USER_ID_PATH)
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true, example = "1") @PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Toggle user enabled status", description = "Toggles the 'enabled' status of a user account (enabled/disabled).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User enabled status toggled successfully"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(USER_TOGGLE_ENABLE_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> toggleUserEnable(
            @Parameter(description = "ID of the user whose enabled status is to be toggled", required = true, example = "1") @PathVariable Long userId) {
        UserResponseDto updatedUser = userService.toggleEnable(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User enable status toggled successfully",
                HttpStatus.OK.value(),
                updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Toggle user account lock status", description = "Toggles the 'account locked' status of a user account (locked/unlocked).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account lock status toggled successfully"),
            @ApiResponse(responseCode = "404", description = "User not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(USER_TOGGLE_LOCK_PATH)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> toggleUserLockAccount(
            @Parameter(description = "ID of the user whose account lock status is to be toggled", required = true, example = "1") @PathVariable Long userId) {
        UserResponseDto updatedUser = userService.toggleLockAccount(userId);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>(
                "User account lock status toggled successfully",
                HttpStatus.OK.value(),
                updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}