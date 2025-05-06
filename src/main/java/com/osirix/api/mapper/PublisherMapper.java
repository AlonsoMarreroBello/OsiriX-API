package com.osirix.api.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.osirix.api.dto.user.publisher.PublisherRequestDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;
import com.osirix.api.dto.user.staff.StaffResponseDto;
import com.osirix.api.entity.Publisher;
import com.osirix.api.utils.UserType;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
	
	@Mapping(target = "accountNotLocked", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isEnabled", ignore = true)
	@Mapping(target = "lastLogin", ignore = true)
	@Mapping(target = "registerDate", ignore = true)
	@Mapping(target = "assignedAdmin", ignore = true)
	Publisher toEntity(PublisherRequestDto request);
	
	@Mapping(target = "userType", ignore = true)
	PublisherResponseDto toResponse(Publisher publisher);
	
	@AfterMapping
	default void afterMapping(@MappingTarget PublisherResponseDto publisherResponseDto) {
		publisherResponseDto.setUserType(UserType.PUBLISHER.toString());
	}
	
}
