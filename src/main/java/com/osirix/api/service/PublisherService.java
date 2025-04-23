package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.PublisherRequestDto;
import com.osirix.api.dto.PublisherResponseDto;

public interface PublisherService {
	
	List<PublisherResponseDto> getAll();
	PublisherResponseDto getPublisherById(Long publisherId);
	
	PublisherResponseDto createPublisher(PublisherRequestDto request);
	PublisherResponseDto updatePublisher(Long publisherId, PublisherRequestDto request);
	
	void deletePublisher(Long publisherId);

}
