package com.osirix.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.osirix.api.dto.notification.NotificationRequestDto;
import com.osirix.api.dto.notification.NotificationResponseDto;
import com.osirix.api.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
	
	@Mapping(target = "notificationId", ignore = true)
	@Mapping(target = "seen", ignore = true)
	@Mapping(target = "sendDate", ignore = true)
	@Mapping(target = "user", ignore = true)
	Notification toEntity(NotificationRequestDto request);
	

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userId", ignore = true)
	NotificationResponseDto toResponse(Notification notification);

}
