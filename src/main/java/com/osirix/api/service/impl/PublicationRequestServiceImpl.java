package com.osirix.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.naming.directory.InvalidAttributesException;

import org.hibernate.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.request.publication.PublicationRequestDto;
import com.osirix.api.dto.request.publication.PublicationRequestSimpleDto;
import com.osirix.api.dto.request.publication.PublicationResponseDto;
import com.osirix.api.entity.App;
import com.osirix.api.entity.Developer;
import com.osirix.api.entity.PublicationRequest;
import com.osirix.api.entity.Publisher;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.PublicationRequestMapper;
import com.osirix.api.repository.AppRepository;
import com.osirix.api.repository.DeveloperRepository;
import com.osirix.api.repository.PublicationRequestRepository;
import com.osirix.api.repository.StaffRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.PublicationRequestService;
import com.osirix.api.utils.UserType;

@Service
public class PublicationRequestServiceImpl implements PublicationRequestService {

	@Autowired
	PublicationRequestRepository publicationRequestRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	StaffRepository staffRepository;
	
	@Autowired
	AppRepository appRepository;

	@Autowired
	DeveloperRepository developerRepository;
	
	@Autowired
	PublicationRequestMapper publicationRequestMapper;
	
	
	@Override
	public List<PublicationResponseDto> getByUserId(Long userId) {
		return publicationRequestRepository.findByUserId(userId)
					.stream().map(publicationRequestMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<PublicationResponseDto> getByAssignedStaffId(Long adminId) {
		return publicationRequestRepository.findByAssignedStaffId(adminId)
				.stream().map(publicationRequestMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public PublicationResponseDto getById(Long requestId) {
		PublicationRequest pRequest = publicationRequestRepository.findById(requestId)
										   .orElseThrow(() -> new ResourceNotFoundException("Publication request not found"));
		
		return publicationRequestMapper.toResponse(pRequest);
	}

	@Override
	public PublicationResponseDto create(PublicationRequestSimpleDto request) {
		PublicationRequest pRequest = publicationRequestMapper.toEntity(request);
		
		if (request.getUserId() != null) {
			User user = userRepository.findById(request.getUserId())
									.orElseThrow(() -> new ResourceNotFoundException("User not found"));
			
			if (user.getType() == UserType.PUBLISHER) {
				pRequest.setUser(user);
				Publisher publisher = (Publisher) user;
				pRequest.setAssignedStaff(publisher.getAssignedAdmin());
			} else {
				throw new TypeMismatchException("User must be a publisher");
			}
			
		}
		
		if (request.getAppId() != null) {
			App app = appRepository.findById(request.getAppId())
									.orElseThrow(() -> new ResourceNotFoundException("App not found"));
			pRequest.setApp(app);
		}
		
		if (request.getDeveloperName() != null) {
			Developer dev = developerRepository.findByName(request.getDeveloperName())
									.orElseThrow(() -> new ResourceNotFoundException("Developer not found"));
			pRequest.setDeveloper(dev);
		}
		
		PublicationRequest savedRequest = publicationRequestRepository.save(pRequest);
		return publicationRequestMapper.toResponse(savedRequest);
	}

	@Override
	public PublicationResponseDto update(Long requestId, PublicationRequestDto request) {
		PublicationRequest req = publicationRequestRepository.findById(requestId)
											.orElseThrow(() -> new ResourceNotFoundException("Publication request not found"));
		
		req.setRequestStatus(request.getStatus());
		req.setAdminComments(request.getAdminComments());
		
		PublicationRequest updatedReq = publicationRequestRepository.save(req);
		
		return publicationRequestMapper.toResponse(updatedReq);
	}

	@Override
	public void delete(Long requestId) {
		PublicationRequest req = publicationRequestRepository.findById(requestId)
											.orElseThrow(() -> new ResourceNotFoundException("Publication request not found"));
		
		publicationRequestRepository.delete(req);
	}

}
