package com.osirix.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.osirix.api.dto.request.RequestDto;
import com.osirix.api.dto.request.RequestResponseDto;
import com.osirix.api.dto.request.RequestSimpleDto;
import com.osirix.api.entity.Request;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.RequestMapper;
import com.osirix.api.repository.RequestRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.RequestService;

public class RequestServiceImpl implements RequestService {
	
	@Autowired
	RequestMapper requestMapper;
	
	@Autowired
	RequestRepository requestRepository;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public List<RequestResponseDto> getAll() {
		return requestRepository.findAll().stream().map(requestMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public RequestResponseDto getById(Long requestId) {
		Request req = requestRepository.findById(requestId).orElseThrow(() -> new ResourceNotFoundException("Request not found")); 
		return requestMapper.toResponse(req);
	}

	@Override
	public List<RequestResponseDto> getByUserId(Long userId) {
		return requestRepository.findAllByUserId(userId).stream().map(requestMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public RequestResponseDto create(RequestSimpleDto request) {
		Request newRequest = requestMapper.toEntity(request);
		
		User user = userRepository.findById(request.getUserId())
									.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		newRequest.setUser(user);
		
		return requestMapper.toResponse(requestRepository.save(newRequest));
	}

	@Override
	public RequestResponseDto update(Long requestId, RequestDto request) {
		Request oldRequest = requestRepository.findById(requestId)
												.orElseThrow(() -> new ResourceNotFoundException("Request not found"));
		
		oldRequest.setRequestStatus(request.getStatus());
		oldRequest.setAdminComments(request.getAdminComments());
		
		Request updatedRequest = requestRepository.save(oldRequest);
		
		return requestMapper.toResponse(updatedRequest);
	}

	@Override
	public void delete(Long requestId) {
		Request request = requestRepository.findById(requestId)
											.orElseThrow(() -> new ResourceNotFoundException("Request not found"));
		
		requestRepository.delete(request);
	}

}
