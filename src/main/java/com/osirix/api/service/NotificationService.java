package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.notification.NotificationResponseDto;

public interface NotificationService {
	
	NotificationResponseDto getById(Long id);
	List<NotificationResponseDto> getNotificationsByUserId(Long userId);
	
	NotificationResponseDto sendNotification(Long userId, String message);
	NotificationResponseDto markNotificationAsSeen(Long notificationId);
	
	void deleteNotification(Long id);
	

}
