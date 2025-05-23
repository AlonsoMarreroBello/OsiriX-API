package com.osirix.api.mapper;

import java.time.LocalDate;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.osirix.api.dto.request.publication.PublicationRequestDto;
import com.osirix.api.dto.request.publication.PublicationRequestSimpleDto;
import com.osirix.api.dto.request.publication.PublicationResponseDto;
import com.osirix.api.entity.PublicationRequest;
import com.osirix.api.entity.RequestStatus;

@Mapper(componentModel = "spring", uses = {AppMapper.class, UserMapper.class})
public interface PublicationRequestMapper {

	@Mapping(target = "requestDate", ignore = true)
	@Mapping(target = "requestId", ignore = true)
	@Mapping(target = "requestStatus", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "adminComments", ignore = true)
	@Mapping(target = "app", ignore = true)
	@Mapping(target = "assignedStaff", ignore = true)
	@Mapping(target = "developer", ignore = true)
	PublicationRequest toEntity(PublicationRequestSimpleDto request);
	
	@Mapping(target = "requestDate", ignore = true)
	@Mapping(target = "requestId", ignore = true)
	@Mapping(target = "requestStatus", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "app", ignore = true)
	@Mapping(target = "assignedStaff", ignore = true)
	@Mapping(target = "developer", ignore = true)
	PublicationRequest toEntity(PublicationRequestDto request);
	
	@Mapping(target = "assignedStaffId", ignore = true)
	PublicationResponseDto toResponse(PublicationRequest request); 
	
	@AfterMapping
	default void afterMapping(PublicationRequest request) {
		request.setRequestStatus(RequestStatus.PENDANT);
		request.setRequestDate(LocalDate.now());
	}
	
	
}
