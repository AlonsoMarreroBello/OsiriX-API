package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.friendship.FriendshipResponseDto;
import com.osirix.api.service.impl.FriendshipServiceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") 
public class FriendshipController {

    private static final String FRIENDSHIP_RESOURCE = "/friendships";
    private static final String FRIENDSHIP_ID_PATH = FRIENDSHIP_RESOURCE + "/{friendshipId}";
    private static final String FRIENDSHIPS_BY_USER_PATH = FRIENDSHIP_RESOURCE + "/by-user/{userId}";

    @Autowired
    FriendshipServiceimpl friendshipService;

    @GetMapping(value = FRIENDSHIP_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong friendships....");
    }

    @GetMapping(FRIENDSHIP_ID_PATH)
    public ResponseEntity<ApiResponseDto<FriendshipResponseDto>> getFriendshipById(@PathVariable Long friendshipId) {
        FriendshipResponseDto friendship = friendshipService.getById(friendshipId);
        ApiResponseDto<FriendshipResponseDto> response = new ApiResponseDto<>(
                "Friendship fetched successfully",
                HttpStatus.OK.value(),
                friendship);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(FRIENDSHIPS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<FriendshipResponseDto>>> getFriendshipsByUserId(@PathVariable Long userId) {
        List<FriendshipResponseDto> friendships = friendshipService.getByUserId(userId);
        ApiResponseDto<List<FriendshipResponseDto>> response = new ApiResponseDto<>(
                "Friendships for user fetched successfully",
                HttpStatus.OK.value(),
                friendships);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(FRIENDSHIP_RESOURCE) 
    public ResponseEntity<ApiResponseDto<FriendshipResponseDto>> sendFriendshipRequest(
            @RequestParam Long senderId,
            @RequestParam String username) { 
        FriendshipResponseDto friendshipRequest = friendshipService.sendFriendshipRequest(senderId, username);
        ApiResponseDto<FriendshipResponseDto> response = new ApiResponseDto<>(
                "Friendship request sent successfully",
                HttpStatus.CREATED.value(),
                friendshipRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(FRIENDSHIP_ID_PATH)
    public ResponseEntity<Void> deleteFriendship(@PathVariable Long friendshipId) {
        friendshipService.deleteFriendship(friendshipId);
        return ResponseEntity.noContent().build();
    }
}