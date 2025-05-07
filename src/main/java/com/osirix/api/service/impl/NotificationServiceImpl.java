package com.osirix.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.notification.NotificationRequestDto;
import com.osirix.api.dto.notification.NotificationResponseDto;
import com.osirix.api.entity.Notification;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.NotificationMapper;
import com.osirix.api.repository.NotificationRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.NotificationService;

@Service
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
		
		NotificationResponseDto response = notificationMapper.toResponse(notification);
		
		response.setId(notification.getNotificationId());
		response.setUserId(notification.getUser().getId());
		
		return response;
	}

	@Override
	public List<NotificationResponseDto> getNotificationsByUserId(Long userId) {
		List<Notification> notifications = notificationRepository.findByUserId(userId).stream().collect(Collectors.toList());
		
		List<NotificationResponseDto> response = new ArrayList<>();
		
		for (Notification notification : notifications) {
			NotificationResponseDto notifiResp = notificationMapper.toResponse(notification);
			
			notifiResp.setId(notification.getNotificationId());
			notifiResp.setUserId(notification.getUser().getId());
			
			response.add(notifiResp);
		}
		
		
		return response;
	}

	@Override
	public List<NotificationResponseDto> getNotSeenNotificationsByUserId(Long userId) {
		List<Notification> notifications = notificationRepository.findByUserIdNotSeen(userId).stream().collect(Collectors.toList());
		
		List<NotificationResponseDto> response = new ArrayList<>();
		
		for (Notification notification : notifications) {
			NotificationResponseDto notifiResp = notificationMapper.toResponse(notification);
			
			notifiResp.setId(notification.getNotificationId());
			notifiResp.setUserId(notification.getUser().getId());
			
			response.add(notifiResp);
		}
		
		
		return response;
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
		
		Notification savedNotification = notificationRepository.save(notification);

		NotificationResponseDto response = notificationMapper.toResponse(savedNotification);
		
		response.setId(notification.getNotificationId());
		response.setUserId(notification.getUser().getId());
		
		return response;
	}

	@Override
	public void deleteNotification(Long id) {
		Notification notification = notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		notificationRepository.delete(notification);
	}

}
