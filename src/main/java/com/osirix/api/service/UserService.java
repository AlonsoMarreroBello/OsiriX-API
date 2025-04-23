package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.UserRequestDto;
import com.osirix.api.dto.UserResponseDto;

public interface UserService {
	
	List<UserResponseDto> getAll();
	List<UserResponseDto> getNormalUsers();	
	UserResponseDto getUserById(Long userId);
	
	UserResponseDto updateUser(Long userId, UserRequestDto request);
	
	void deleteUser(Long userId);
	
	UserResponseDto toggleEnable(Long userId);
	UserResponseDto toggleLockAccount(Long userId);

}
