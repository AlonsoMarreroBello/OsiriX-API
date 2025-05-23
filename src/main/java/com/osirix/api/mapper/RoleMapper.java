package com.osirix.api.mapper;

import org.mapstruct.Mapper;

import com.osirix.api.dto.role.RoleResponseDto;
import com.osirix.api.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	RoleResponseDto toDto(Role role);
	
}
