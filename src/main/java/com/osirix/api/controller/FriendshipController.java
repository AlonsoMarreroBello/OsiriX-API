package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.friendship.FriendshipResponseDto;
import com.osirix.api.service.impl.FriendshipServiceimpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.Valid; // Not needed as there are no @RequestBody DTOs

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Friendship Management", description = "APIs for managing user friendships")
public class FriendshipController {

    private static final String FRIENDSHIP_RESOURCE = "/friendships";
    private static final String FRIENDSHIP_ID_PATH = FRIENDSHIP_RESOURCE + "/{friendshipId}";
    private static final String FRIENDSHIPS_BY_USER_PATH = FRIENDSHIP_RESOURCE + "/by-user/{userId}";

    @Autowired
    FriendshipServiceimpl friendshipService;

    @Operation(summary = "Ping Friendship Controller", description = "A simple ping endpoint to check if the Friendship controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = FRIENDSHIP_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong friendships....");
    }

    @Operation(summary = "Get friendship by ID", description = "Retrieves a specific friendship by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved friendship"),
            @ApiResponse(responseCode = "404", description = "Friendship not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(FRIENDSHIP_ID_PATH)
    public ResponseEntity<ApiResponseDto<FriendshipResponseDto>> getFriendshipById(
            @Parameter(description = "ID of the friendship to retrieve", required = true, example = "1") @PathVariable Long friendshipId) {
        FriendshipResponseDto friendship = friendshipService.getById(friendshipId);
        ApiResponseDto<FriendshipResponseDto> response = new ApiResponseDto<>(
                "Friendship fetched successfully",
                HttpStatus.OK.value(),
                friendship);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get friendships by user ID", description = "Retrieves all friendships for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of friendships for the user"),
            @ApiResponse(responseCode = "404", description = "User not found or user has no friendships"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(FRIENDSHIPS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<FriendshipResponseDto>>> getFriendshipsByUserId(
            @Parameter(description = "ID of the user whose friendships are to be retrieved", required = true, example = "101") @PathVariable Long userId) {
        List<FriendshipResponseDto> friendships = friendshipService.getByUserId(userId);
        ApiResponseDto<List<FriendshipResponseDto>> response = new ApiResponseDto<>(
                "Friendships for user fetched successfully",
                HttpStatus.OK.value(),
                friendships);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Send a friendship request", description = "Sends a friendship request from a sender to a user identified by their username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Friendship request sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g., sender trying to friend themselves, or request already exists)"),
            @ApiResponse(responseCode = "404", description = "Sender or receiver user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(FRIENDSHIP_RESOURCE)
    public ResponseEntity<ApiResponseDto<FriendshipResponseDto>> sendFriendshipRequest(
            @Parameter(description = "ID of the user sending the request", required = true, example = "101") @RequestParam Long senderId,
            @Parameter(description = "Username of the user to whom the request is being sent", required = true, example = "receiverUser") @RequestParam String username) {
        FriendshipResponseDto friendshipRequest = friendshipService.sendFriendshipRequest(senderId, username);
        ApiResponseDto<FriendshipResponseDto> response = new ApiResponseDto<>(
                "Friendship request sent successfully",
                HttpStatus.CREATED.value(),
                friendshipRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Accept a friendship request", description = "Accepts a pending friendship request identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Friendship request accepted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Friendship request not found with the given ID, or it's not in a pending state"),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g., trying to accept an already accepted or non-existent request)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(FRIENDSHIP_ID_PATH)
    public ResponseEntity<Void> acceptFriendshipRequest(
            @Parameter(description = "ID of the friendship request to accept", required = true, example = "1") @PathVariable Long friendshipId) {
        friendshipService.acceptFriendship(friendshipId);
        return ResponseEntity.noContent().build();
    }
    

    @Operation(summary = "Delete a friendship or decline a request", description = "Deletes an existing friendship or declines a pending friendship request by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Friendship deleted or request declined successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Friendship or request not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(FRIENDSHIP_ID_PATH)
    public ResponseEntity<Void> deleteFriendship(
            @Parameter(description = "ID of the friendship or friendship request to delete/decline", required = true, example = "1") @PathVariable Long friendshipId) {
        friendshipService.deleteFriendship(friendshipId);
        return ResponseEntity.noContent().build();
    }
}