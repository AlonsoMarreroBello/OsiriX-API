package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.notification.NotificationRequestDto;
import com.osirix.api.dto.notification.NotificationResponseDto;
import com.osirix.api.service.impl.NotificationServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") 
public class NotificationController {

    private static final String NOTIFICATION_RESOURCE = "/notifications";
    private static final String NOTIFICATION_ID_PATH = NOTIFICATION_RESOURCE + "/{notificationId}";
    private static final String NOTIFICATIONS_BY_USER_PATH = NOTIFICATION_RESOURCE + "/by-user/{userId}";
    private static final String NOTIFICATIONS_BY_USER_NOT_SEEN_PATH = NOTIFICATIONS_BY_USER_PATH + "/not-seen";
    private static final String NOTIFICATION_MARK_AS_SEEN_PATH = NOTIFICATION_ID_PATH + "/mark-as-seen";

    @Autowired
    NotificationServiceImpl notificationService;

    @GetMapping(value = NOTIFICATION_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong notifications....");
    }

    @GetMapping(NOTIFICATION_ID_PATH)
    public ResponseEntity<ApiResponseDto<NotificationResponseDto>> getNotificationById(@PathVariable Long notificationId) {
        NotificationResponseDto notification = notificationService.getById(notificationId);
        ApiResponseDto<NotificationResponseDto> response = new ApiResponseDto<>(
                "Notification fetched successfully",
                HttpStatus.OK.value(),
                notification);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(NOTIFICATIONS_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<NotificationResponseDto>>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUserId(userId);
        ApiResponseDto<List<NotificationResponseDto>> response = new ApiResponseDto<>(
                "Notifications for user fetched successfully",
                HttpStatus.OK.value(),
                notifications);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(NOTIFICATIONS_BY_USER_NOT_SEEN_PATH)
    public ResponseEntity<ApiResponseDto<List<NotificationResponseDto>>> getNotificationsByUserIdNotSeen(@PathVariable Long userId) {
    	List<NotificationResponseDto> notifications = notificationService.getNotSeenNotificationsByUserId(userId);
    	ApiResponseDto<List<NotificationResponseDto>> response = new ApiResponseDto<>(
    			"Notifications for user fetched successfully",
    			HttpStatus.OK.value(),
    			notifications);
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(NOTIFICATION_RESOURCE)
    public ResponseEntity<ApiResponseDto<NotificationResponseDto>> sendNotification(@RequestBody NotificationRequestDto notificationRequestDto) {
        NotificationResponseDto sentNotification = notificationService.sendNotification(notificationRequestDto);
        ApiResponseDto<NotificationResponseDto> response = new ApiResponseDto<>(
                "Notification sent successfully",
                HttpStatus.CREATED.value(),
                sentNotification);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(NOTIFICATION_MARK_AS_SEEN_PATH)
    public ResponseEntity<ApiResponseDto<NotificationResponseDto>> markNotificationAsSeen(@PathVariable Long notificationId) {
        NotificationResponseDto updatedNotification = notificationService.markNotificationAsSeen(notificationId);
        ApiResponseDto<NotificationResponseDto> response = new ApiResponseDto<>(
                "Notification marked as seen successfully",
                HttpStatus.OK.value(),
                updatedNotification);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(NOTIFICATION_ID_PATH)
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}