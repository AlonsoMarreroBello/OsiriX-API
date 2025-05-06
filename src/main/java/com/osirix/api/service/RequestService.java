package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.request.RequestDto;
import com.osirix.api.dto.request.RequestResponseDto;
import com.osirix.api.dto.request.RequestSimpleDto;

public interface RequestService {
	
	List<RequestResponseDto> getAll();
	
	RequestResponseDto getById(Long requestId);
	
	List<RequestResponseDto> getByUserId(Long userId);
	
	RequestResponseDto create(RequestSimpleDto request);
	
	RequestResponseDto update(Long requestId, RequestDto request);
	
	void delete(Long requestId);

}
