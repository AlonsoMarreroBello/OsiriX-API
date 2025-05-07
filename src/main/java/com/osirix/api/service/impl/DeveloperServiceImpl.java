package com.osirix.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.developer.DeveloperRequestDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;
import com.osirix.api.entity.Developer;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.DeveloperMapper;
import com.osirix.api.repository.DeveloperRepository;
import com.osirix.api.service.DeveloperService;

@Service
public class DeveloperServiceImpl implements DeveloperService {
	
	@Autowired
	DeveloperRepository developerRepository;
	
	@Autowired
	DeveloperMapper developerMapper;

	@Override
	public List<DeveloperResponseDto> getAll() {
		return developerRepository.findAll().stream().map(developerMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public DeveloperResponseDto getById(Long DeveloperId) {
		Developer developer =  developerRepository.findById(DeveloperId).orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
	
		return developerMapper.toResponse(developer);
	}

	@Override
	public DeveloperResponseDto createDeveloper(DeveloperRequestDto request) {
		Developer developer = developerMapper.toEntity(request);
		
		return developerMapper.toResponse(developerRepository.save(developer));
	}

	@Override
	public DeveloperResponseDto updateDeveloper(Long developerId, DeveloperRequestDto request) {
		Developer developer = developerRepository.findById(developerId).orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
		
		developer.setName(request.getName());
		
		Developer updatedDeveloper = developerRepository.save(developer);
		
		return developerMapper.toResponse(updatedDeveloper);
		
	}

	@Override
	public void deleteDeveloper(Long developerId) {
		Developer developer = developerRepository.findById(developerId).orElseThrow(()-> new ResourceNotFoundException("Developer not found"));
		
		developerRepository.delete(developer);
	}

}
