package com.osirix.api.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.osirix.api.dto.notification.NotificationRequestDto;
import com.osirix.api.dto.notification.NotificationResponseDto;
import com.osirix.api.entity.Notification;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.NotificationMapper;
import com.osirix.api.repository.NotificationRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	NotificationRepository notificationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	NotificationMapper notificationMapper;

	@Override
	public NotificationResponseDto getById(Long id) {
		Notification notification = notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
		
		return notificationMapper.toResponse(notification);
	}

	@Override
	public List<NotificationResponseDto> getNotificationsByUserId(Long userId) {
		List<NotificationResponseDto> notifications = notificationRepository.findByUserId(userId).stream().map(notificationMapper::toResponse).collect(Collectors.toList());
		
		return notifications;
	}

	@Override
	public NotificationResponseDto sendNotification(NotificationRequestDto request) {
		
		Notification notification = notificationMapper.toEntity(request);
		
		User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		notification.setUser(user);
		notification.setSendDate(LocalDateTime.now());
		notification.setSeen(false);
		
		return notificationMapper.toResponse(notificationRepository.save(notification));
	}

	@Override
	public NotificationResponseDto markNotificationAsSeen(Long notificationId) {

		Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		notification.setSeen(true);
		
		return notificationMapper.toResponse(notificationRepository.save(notification));
		
	}

	@Override
	public void deleteNotification(Long id) {
		Notification notification = notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		notificationRepository.delete(notification);
	}

}
