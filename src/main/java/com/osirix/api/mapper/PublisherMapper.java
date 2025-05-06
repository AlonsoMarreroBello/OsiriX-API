package com.osirix.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.osirix.api.dto.user.publisher.PublisherRequestDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;
import com.osirix.api.entity.Publisher;

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
	
}
