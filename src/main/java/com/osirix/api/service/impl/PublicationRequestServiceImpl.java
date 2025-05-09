package com.osirix.api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.osirix.api.entity.RequestStatus;
import com.osirix.api.entity.User;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.PublicationRequestMapper;
import com.osirix.api.mapper.UserMapper;
import com.osirix.api.repository.AppRepository;
import com.osirix.api.repository.DeveloperRepository;
import com.osirix.api.repository.PublicationRequestRepository;
import com.osirix.api.repository.PublisherRepository;
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
	PublisherRepository publisherRepository;
	
	@Autowired
	PublicationRequestMapper publicationRequestMapper;

	@Autowired
	UserMapper userMapper;
	
	
	@Override
	public List<PublicationResponseDto> getByUserId(Long userId) {
		List<PublicationRequest> publicationList = publicationRequestRepository.findByUserId(userId)
					.stream().collect(Collectors.toList());
		
		List<PublicationResponseDto> responseList = new ArrayList<>();
		
		for (PublicationRequest publicationRequest : publicationList) {
			PublicationResponseDto response = publicationRequestMapper.toResponse(publicationRequest);

			Optional<Publisher> publisher = publisherRepository.findById(response.getUser().getId());
			
			if (publisher.isPresent()) {
				response.getUser().setUserType(UserType.PUBLISHER.toString());
				response.setAssignedStaffId(publisher.get().getId());
			}
			
			responseList.add(response);
		}
		
		return responseList;
	}

	@Override
	public List<PublicationResponseDto> getByAssignedStaffId(Long adminId) {
		List<PublicationRequest> publicationList = publicationRequestRepository.findByAssignedStaffId(adminId)
				.stream().collect(Collectors.toList());
	
		List<PublicationResponseDto> responseList = new ArrayList<>();
		
		for (PublicationRequest publicationRequest : publicationList) {
			PublicationResponseDto response = publicationRequestMapper.toResponse(publicationRequest);
	
			Optional<Publisher> publisher = publisherRepository.findById(response.getUser().getId());
			
			if (publisher.isPresent()) {
				response.getUser().setUserType(UserType.PUBLISHER.toString());
				response.setAssignedStaffId(publisher.get().getId());
			}
			
			responseList.add(response);
		}
		
		return responseList;
		
	}

	@Override
	public PublicationResponseDto getById(Long requestId) {
		PublicationRequest pRequest = publicationRequestRepository.findById(requestId)
										   .orElseThrow(() -> new ResourceNotFoundException("Publication request not found"));
		
		PublicationResponseDto response = publicationRequestMapper.toResponse(pRequest);
		
		Optional<Publisher> publisher = publisherRepository.findById(response.getUser().getId());
		
		if (publisher.isPresent()) {
			response.getUser().setUserType(UserType.PUBLISHER.toString());
			response.setAssignedStaffId(publisher.get().getId());
		}
		
		return response;
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
		
		pRequest.setRequestDate(LocalDate.now());
		pRequest.setRequestStatus(RequestStatus.PENDANT);
		
		PublicationRequest savedRequest = publicationRequestRepository.save(pRequest);
		PublicationResponseDto responseSavedRequest = publicationRequestMapper.toResponse(savedRequest);
		
		responseSavedRequest.getUser().setUserType("PUBLISHER");
		responseSavedRequest.setAssignedStaffId(pRequest.getAssignedStaff().getId());
		
		return responseSavedRequest;
	}

	@Override
	public PublicationResponseDto update(Long requestId, PublicationRequestDto request) {
		PublicationRequest req = publicationRequestRepository.findById(requestId)
											.orElseThrow(() -> new ResourceNotFoundException("Publication request not found"));
		
		req.setRequestStatus(request.getStatus());
		req.setAdminComments(request.getAdminComments());
		
		
		
		PublicationRequest updatedReq = publicationRequestRepository.save(req);
		
		PublicationResponseDto response = publicationRequestMapper.toResponse(updatedReq);
		
		Optional<Publisher> publisher = publisherRepository.findById(response.getUser().getId());
		
		if (publisher.isPresent()) {
			response.getUser().setUserType(UserType.PUBLISHER.toString());
			response.setAssignedStaffId(publisher.get().getId());
		}
		
		return response;
	}

	@Override
	public void delete(Long requestId) {
		PublicationRequest req = publicationRequestRepository.findById(requestId)
											.orElseThrow(() -> new ResourceNotFoundException("Publication request not found"));
		
		publicationRequestRepository.delete(req);
	}

}
