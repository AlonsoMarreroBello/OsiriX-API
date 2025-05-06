package com.osirix.api.mapper;

import java.time.LocalDate;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.osirix.api.dto.request.RequestDto;
import com.osirix.api.dto.request.RequestResponseDto;
import com.osirix.api.dto.request.RequestSimpleDto;
import com.osirix.api.entity.Request;
import com.osirix.api.entity.RequestStatus;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RequestMapper {
	
	@Mapping(target = "requestDate", ignore = true)
	@Mapping(target = "requestId", ignore = true)
	@Mapping(target = "requestStatus", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "adminComments", ignore = true)
	Request toEntity(RequestSimpleDto requestDto);
	
	@Mapping(target = "requestDate", ignore = true)
	@Mapping(target = "requestId", ignore = true)
	@Mapping(target = "requestStatus", ignore = true)
	@Mapping(target = "user", ignore = true)
	Request toEntity(RequestDto request);
	
	RequestResponseDto toResponse(Request request);
	
	@AfterMapping
	default void afterMapping(@MappingTarget Request request) {
		request.setRequestDate(LocalDate.now());
		request.setRequestStatus(RequestStatus.PENDANT);
	}

}
