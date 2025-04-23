package com.osirix.api.service;

import com.osirix.api.dto.AuthLoginRequestDto;
import com.osirix.api.dto.AuthResponseDto;
import com.osirix.api.dto.UserRequestDto;
import com.osirix.api.dto.UserResponseDto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
	
	boolean canUserUploadToApp(Long appId);
	boolean canUserDownloadApp(Long appId);
	
	AuthResponseDto login(AuthLoginRequestDto request);
	
	UserResponseDto register(UserRequestDto request);
	
	Authentication authenticate(String name, String password);

}
