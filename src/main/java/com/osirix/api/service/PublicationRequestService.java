package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.request.publication.PublicationRequestDto;
import com.osirix.api.dto.request.publication.PublicationRequestSimpleDto;
import com.osirix.api.dto.request.publication.PublicationResponseDto;

public interface PublicationRequestService {
	
	List<PublicationResponseDto> getByUserId(Long userId);
	List<PublicationResponseDto> getByAssignedStaffId(Long adminId);
	PublicationResponseDto getById(Long requestId);
	
	PublicationResponseDto create(PublicationRequestSimpleDto request);
	PublicationResponseDto update(Long requestId, PublicationRequestDto request);
	
	void delete(Long requestId);
	
	

}
