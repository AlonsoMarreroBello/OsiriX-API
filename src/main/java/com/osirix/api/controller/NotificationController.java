package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.notification.NotificationRequestDto;
import com.osirix.api.dto.notification.NotificationResponseDto;
import com.osirix.api.service.impl.NotificationServiceImpl;

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
@Tag(name = "Notification Management", description = "APIs for managing user notifications")
public class NotificationController {

    private static final String NOTIFICATION_RESOURCE = "/notifications";
    private static final String NOTIFICATION_ID_PATH = NOTIFICATION_RESOURCE + "/{notificationId}";
    private static final String NOTIFICATIONS_BY_USER_PATH = NOTIFICATION_RESOURCE + "/by-user/{userId}";
    private static final String NOTIFICATIONS_BY_USER_NOT_SEEN_PATH = NOTIFICATIONS_BY_USER_PATH + "/not-seen";
    private static final String NOTIFICATION_MARK_AS_SEEN_PATH = NOTIFICATION_ID_PATH + "/mark-as-seen";

    @Autowired
    NotificationServiceImpl notificationService;

    @Operation(summary = "Ping Notification Controller", description = "A simple ping endpoint to check if the Notification controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = NOTIFICATION_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong notifications....");
    }

    @Operation(summary = "Get notification by ID", description = "Retrieves a specific notification by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notification"),
            @ApiResponse(responseCode = "404", description = "Notification not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(NOTIFICATION_ID_PATH)
    public ResponseEntity<ApiResponseDto<NotificationResponseDto>> getNotificationById(
            @Parameter(description = "ID of the notification to retrieve", required = true, example = "1") @PathVariable Long notificationId) {
        NotificationResponseDto notification = notificationService.getById(notificationId);
        ApiResponseDto<NotificationResponseDto> response = new ApiResponseDto<>(
                "Notification fetched successfully",
                HttpStatus.OK.value(),
                notification);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get all notifications for a user", description = "Retrieves all notifications for a specific user, regardless of seen status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications for the user"),
            @ApiResponse(responseCode = "404", description = "User not found or user has no notifications"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(NOTIFICATIONS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<NotificationResponseDto>>> getNotificationsByUserId(
            @Parameter(description = "ID of the user whose notifications are to be retrieved", required = true, example = "101") @PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUserId(userId);
        ApiResponseDto<List<NotificationResponseDto>> response = new ApiResponseDto<>(
                "Notifications for user fetched successfully",
                HttpStatus.OK.value(),
                notifications);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get unread notifications for a user", description = "Retrieves all notifications for a specific user that have not yet been marked as seen.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved unread notifications for the user"),
            @ApiResponse(responseCode = "404", description = "User not found or user has no unread notifications"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(NOTIFICATIONS_BY_USER_NOT_SEEN_PATH)
    public ResponseEntity<ApiResponseDto<List<NotificationResponseDto>>> getNotificationsByUserIdNotSeen(
            @Parameter(description = "ID of the user whose unread notifications are to be retrieved", required = true, example = "101") @PathVariable Long userId) {
    	List<NotificationResponseDto> notifications = notificationService.getNotSeenNotificationsByUserId(userId);
    	ApiResponseDto<List<NotificationResponseDto>> response = new ApiResponseDto<>(
    			"Unread notifications for user fetched successfully",
    			HttpStatus.OK.value(),
    			notifications);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Send a notification", description = "Creates and sends a new notification based on the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid notification data provided (e.g., missing recipient or content)"),
            @ApiResponse(responseCode = "404", description = "Recipient user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(NOTIFICATION_RESOURCE)
    public ResponseEntity<ApiResponseDto<NotificationResponseDto>> sendNotification(
            @Valid @RequestBody NotificationRequestDto notificationRequestDto) {
        NotificationResponseDto sentNotification = notificationService.sendNotification(notificationRequestDto);
        ApiResponseDto<NotificationResponseDto> response = new ApiResponseDto<>(
                "Notification sent successfully",
                HttpStatus.CREATED.value(),
                sentNotification);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Mark a notification as seen", description = "Updates the status of a specific notification to 'seen'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as seen successfully"),
            @ApiResponse(responseCode = "404", description = "Notification not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping(NOTIFICATION_MARK_AS_SEEN_PATH)
    public ResponseEntity<ApiResponseDto<NotificationResponseDto>> markNotificationAsSeen(
            @Parameter(description = "ID of the notification to mark as seen", required = true, example = "1") @PathVariable Long notificationId) {
        NotificationResponseDto updatedNotification = notificationService.markNotificationAsSeen(notificationId);
        ApiResponseDto<NotificationResponseDto> response = new ApiResponseDto<>(
                "Notification marked as seen successfully",
                HttpStatus.OK.value(),
                updatedNotification);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a notification", description = "Deletes a notification by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Notification not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(NOTIFICATION_ID_PATH)
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "ID of the notification to delete", required = true, example = "1") @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}