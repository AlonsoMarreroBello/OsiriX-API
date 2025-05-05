package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.developer.DeveloperRequestDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;

public interface DeveloperService {
	
	List<DeveloperResponseDto> getAll();
	DeveloperResponseDto getById(Long DeveloperId);
	
	DeveloperResponseDto createDeveloper(DeveloperRequestDto request);
	DeveloperResponseDto updateDeveloper(Long developerId, DeveloperRequestDto request);
	
	void deleteDeveloper(Long developerId);
	
}
