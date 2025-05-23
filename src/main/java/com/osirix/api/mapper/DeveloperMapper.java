package com.osirix.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.osirix.api.dto.developer.DeveloperRequestDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;
import com.osirix.api.entity.Developer;

@Mapper(componentModel = "spring")
public interface DeveloperMapper {
	
	DeveloperResponseDto toResponse(Developer developer);

	@Mapping(target = "id", ignore = true)
	Developer toEntity(DeveloperRequestDto request);
	
}
