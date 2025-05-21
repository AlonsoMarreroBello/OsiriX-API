package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.role.RoleResponseDto;
import com.osirix.api.dto.user.staff.StaffRequestDto;
import com.osirix.api.dto.user.staff.StaffResponseDto;

public interface StaffService {
	
	List<StaffResponseDto> getAll();
	
	StaffResponseDto createStaff(StaffRequestDto request);
	StaffResponseDto update(Long staffId, StaffRequestDto request);
	
	List<RoleResponseDto>getAllRoles();

}
