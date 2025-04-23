package com.osirix.api.service;

import com.osirix.api.dto.DeveloperRequestDto;
import com.osirix.api.dto.DeveloperResponseDto;

public interface DeveloperService {
	
	DeveloperResponseDto getAll();
	DeveloperResponseDto getById(Long DeveloperId);
	
	DeveloperResponseDto createDeveloper(DeveloperRequestDto request);
	DeveloperResponseDto updateDeveloper(Long developerId, DeveloperRequestDto request);
	
	void deleteDeveloper(Long developerId);
	
}
