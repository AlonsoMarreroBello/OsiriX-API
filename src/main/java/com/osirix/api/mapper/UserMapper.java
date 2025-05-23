package com.osirix.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;
import com.osirix.api.dto.user.UserSimpleResponseDto;
import com.osirix.api.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	@Mapping(target = "accountNotLocked", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isEnabled", ignore = true)
	@Mapping(target = "lastLogin", ignore = true)
	@Mapping(target = "registerDate", ignore = true)
	User ToEntity(UserRequestDto request);
	
	@Mapping(target = "userType", ignore = true)
	UserResponseDto toDto(User user);
	
	UserSimpleResponseDto toSimpleDto(User user);

}
