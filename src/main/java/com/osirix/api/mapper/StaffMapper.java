package com.osirix.api.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.osirix.api.dto.user.staff.StaffRequestDto;
import com.osirix.api.dto.user.staff.StaffResponseDto;
import com.osirix.api.entity.Staff;
import com.osirix.api.utils.UserType;

@Mapper(componentModel = "spring", uses = PublisherMapperImpl.class)
public interface StaffMapper {

	@Mapping(target = "userType", ignore = true)
	StaffResponseDto toResponse(Staff staff);
	
	@AfterMapping
	default void afterMapping(@MappingTarget StaffResponseDto staffResponseDto) {
		staffResponseDto.setUserType(UserType.STAFF.toString());
	}

	@Mapping(target = "accountNotLocked", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isEnabled", ignore = true)
	@Mapping(target = "lastLogin", ignore = true)
	@Mapping(target = "registerDate", ignore = true)
	@Mapping(target = "assignedPublishers", ignore = true)
	@Mapping(target = "roles", ignore = true)
	Staff toEntity(StaffRequestDto request);
	
}
