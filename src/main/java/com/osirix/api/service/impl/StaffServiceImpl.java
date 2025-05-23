package com.osirix.api.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.role.RoleResponseDto;
import com.osirix.api.dto.user.staff.StaffRequestDto;
import com.osirix.api.dto.user.staff.StaffResponseDto;
import com.osirix.api.entity.Publisher;
import com.osirix.api.entity.Role;
import com.osirix.api.entity.Staff;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.RoleMapper;
import com.osirix.api.mapper.StaffMapper;
import com.osirix.api.repository.PublisherRepository;
import com.osirix.api.repository.RoleRepository;
import com.osirix.api.repository.StaffRepository;
import com.osirix.api.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {
	
	@Autowired
	StaffMapper staffMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	StaffRepository staffRepository;
	
	@Autowired 
	PublisherRepository publisherRepository;

	@Autowired 
	RoleRepository roleRepository;
	
	@Autowired
	RoleMapper roleMapper;
	
	@Override
	public List<StaffResponseDto> getAll() {
		return staffRepository.findAll().stream().map(staffMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public StaffResponseDto createStaff(StaffRequestDto request) {
		Staff staff = staffMapper.toEntity(request);
		
		staff.setAccountNotLocked(true);
		staff.setIsEnabled(true);
		staff.setRegisterDate(LocalDate.now());
		
		if (request.getAsignedPublishersIds() != null && !request.getAsignedPublishersIds().isEmpty()) {
	        List<Publisher> publishers = publisherRepository.findAllById(request.getAsignedPublishersIds());
	        
	        if (publishers.size() != request.getAsignedPublishersIds().size()) {
	            throw new ResourceNotFoundException("Some publishers not found");
	        }

	        staff.setAssignedPublishers(publishers);
	    }

	    if (request.getRoleNames() != null && !request.getRoleNames().isEmpty()) {
	        Set<Role> roles = roleRepository.findByRoleNameIn(request.getRoleNames());
	        
	        if (roles.size() != request.getRoleNames().size()) {
	            throw new ResourceNotFoundException("Some roles not found");
	        }

	        staff.setRoles(roles);
	    }
	    
	    staff.setPassword(passwordEncoder.encode(request.getPassword()));
		
		return staffMapper.toResponse(staffRepository.save(staff));
	}

	@Override
	public StaffResponseDto update(Long staffId, StaffRequestDto request) {
		Staff staff = staffRepository.findById(staffId)
		        .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));

		    if (request.getUsername() != null && !request.getUsername().isBlank()) {
		        staff.setUsername(request.getUsername());
		    }

		    if (request.getEmail() != null && !request.getEmail().isBlank()) {
		        staff.setEmail(request.getEmail());
		    }

		    if (request.getPassword() != null && !request.getPassword().isBlank()) {
		        staff.setPassword(passwordEncoder.encode(request.getPassword()));
		    }

		    if (request.getAsignedPublishersIds() != null && !request.getAsignedPublishersIds().isEmpty()) {
		        List<Publisher> publishers = publisherRepository.findAllById(request.getAsignedPublishersIds());
		        
		        if (publishers.size() != request.getAsignedPublishersIds().size()) {
		            throw new ResourceNotFoundException("Some publishers not found");
		        }

		        staff.setAssignedPublishers(publishers);
		    }

		    if (request.getRoleNames() != null && !request.getRoleNames().isEmpty()) {
		        Set<Role> roles = roleRepository.findByRoleNameIn(request.getRoleNames());
		        
		        if (roles.size() != request.getRoleNames().size()) {
		            throw new ResourceNotFoundException("Some roles not found");
		        }

		        staff.setRoles(roles);
		    }

		    Staff savedStaff = staffRepository.save(staff);

		    return staffMapper.toResponse(savedStaff);
	}

	@Override
	public List<RoleResponseDto> getAllRoles() {
		return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toList());
	}

}
