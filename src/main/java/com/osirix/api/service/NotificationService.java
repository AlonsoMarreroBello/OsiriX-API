package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.notification.NotificationRequestDto;
import com.osirix.api.dto.notification.NotificationResponseDto;

/**
 * Service interface for managing user notifications.
 * This interface defines the contract for operations related to notifications,
 * such as retrieving, sending, marking as seen, and deleting notifications.
 * It uses {@link NotificationRequestDto} for input and {@link NotificationResponseDto} for output.
 *
 * @author Alonso Marrero Bello
 */
public interface NotificationService {

    /**
     * Retrieves a specific notification by its unique identifier.
     *
     * @param id The ID of the notification to retrieve.
     * @return The {@link NotificationResponseDto} object for the found notification.
     *         May return {@code null} or throw an exception if no notification with the given ID is found.
     */
    NotificationResponseDto getById(Long id);

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param userId The ID of the user whose notifications are to be retrieved.
     * @return A list of {@link NotificationResponseDto} objects representing all notifications for the user.
     *         Returns an empty list if the user has no notifications.
     */
    List<NotificationResponseDto> getNotificationsByUserId(Long userId);

    /**
     * Retrieves all notifications for a specific user that have not yet been marked as seen.
     *
     * @param userId The ID of the user whose unseen notifications are to be retrieved.
     * @return A list of {@link NotificationResponseDto} objects representing the unseen notifications for the user.
     *         Returns an empty list if the user has no unseen notifications.
     */
    List<NotificationResponseDto> getNotSeenNotificationsByUserId(Long userId);

    /**
	 * Sends or creates a new notification. The details of the notification,
	 * including the recipient, are contained within the request object.
	 *
	 * @param request The {@link NotificationRequestDto} object containing the data
	 *                for the new notification.
	 * @return The {@link NotificationResponseDto} object for the newly created
	 *         notification. May throw an exception if notification creation fails.
	 */
    NotificationResponseDto sendNotification(NotificationRequestDto request);

    /**
     * Marks a specific notification as seen or read.
     *
     * @param notificationId The ID of the notification to mark as seen.
     * @return The {@link NotificationResponseDto} object for the updated notification, reflecting its new seen status.
     *         May throw an exception if the notification is not found or cannot be updated.
     */
    NotificationResponseDto markNotificationAsSeen(Long notificationId);

    /**
     * Deletes a notification by its unique identifier.
     *
     * @param id The ID of the notification to delete.
     *         May throw an exception if the notification to delete is not found.
     */
    void deleteNotification(Long id);

}
