package com.osirix.api.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.user.publisher.PublisherRequestDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;
import com.osirix.api.entity.Publisher;
import com.osirix.api.entity.Staff;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.PublisherMapper;
import com.osirix.api.repository.PublisherRepository;
import com.osirix.api.repository.StaffRepository;
import com.osirix.api.service.PublisherService;
import com.osirix.api.utils.UserType;

@Service
public class PublisherServiceImpl implements PublisherService {
	
	@Autowired
	PublisherRepository publisherRepository;

	@Autowired
	StaffRepository staffRepository;
	
	@Autowired
	PublisherMapper publisherMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public List<PublisherResponseDto> getAll() {
		return publisherRepository.findAll().stream().map(publisherMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public PublisherResponseDto getPublisherById(Long publisherId) {
		Publisher publisher = publisherRepository.findById(publisherId)
					.orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
		
		return publisherMapper.toResponse(publisher);
	}

	@Override
	public PublisherResponseDto createPublisher(PublisherRequestDto request) {
		Publisher publisher = publisherMapper.toEntity(request);
		
		publisher.setAccountNotLocked(true);
		publisher.setIsEnabled(false);
		publisher.setLastLogin(null);
		publisher.setRegisterDate(LocalDate.now());
		
		if (request.getAssignedAdminId() != null) {
			Staff staff = staffRepository.findById(request.getAssignedAdminId())
										 .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
			publisher.setAssignedAdmin(staff);
		}
		
		publisher.setPassword(passwordEncoder.encode(request.getPassword()));
		
		Publisher savedPublisher = publisherRepository.save(publisher);
		
		return publisherMapper.toResponse(savedPublisher);
	}

	@Override
	public PublisherResponseDto updatePublisher(Long publisherId, PublisherRequestDto request) {
	    Publisher publisher = publisherRepository.findById(publisherId)
	        .orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));

	    if (request.getUsername() != null && !request.getUsername().isBlank()) {
	        publisher.setUsername(request.getUsername());
	    }

	    if (request.getEmail() != null && !request.getEmail().isBlank()) {
	        publisher.setEmail(request.getEmail());
	    }

	    if (request.getPassword() != null && !request.getPassword().isBlank()) {
	        publisher.setPassword(passwordEncoder.encode(request.getPassword()));
	    }

	    if (request.getNif() != null && !request.getNif().isBlank()) {
	        publisher.setNif(request.getNif());
	    }

	    if (request.getPublisherName() != null && !request.getPublisherName().isBlank()) {
	        publisher.setPublisherName(request.getPublisherName());
	    }

	    if (request.getAddress() != null && !request.getAddress().isBlank()) {
	        publisher.setAddress(request.getAddress());
	    }

	    if (request.getAssignedAdminId() != null) {
	        Long currentAdminId = publisher.getAssignedAdmin() != null ? publisher.getAssignedAdmin().getId() : null;
	        if (!request.getAssignedAdminId().equals(currentAdminId)) {
	            Staff staff = staffRepository.findById(request.getAssignedAdminId())
	                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
	            publisher.setAssignedAdmin(staff);
	        }
	    }
	    
	    Publisher savedPublisher = publisherRepository.save(publisher);
	   
	    return publisherMapper.toResponse(savedPublisher);
	}


	@Override
	public void deletePublisher(Long publisherId) {
		 Publisher publisher = publisherRepository.findById(publisherId)
			        .orElseThrow(() -> new ResourceNotFoundException("Publisher not found"));
		 
		 publisher.removeAssignedAdmin();
		 
		 publisherRepository.delete(publisher);
	}

}
