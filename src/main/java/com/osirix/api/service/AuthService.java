package com.osirix.api.service;

import com.osirix.api.dto.auth.AuthLoginRequestDto;
import com.osirix.api.dto.auth.AuthResponseDto;
import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.dto.user.UserResponseDto;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
	
	boolean canUserUploadToApp(Long appId);
	boolean canUserDownloadApp(Long appId);
	
	AuthResponseDto login(AuthLoginRequestDto request);
	
	AuthResponseDto register(UserRequestDto request);
	
	Authentication authenticate(String name, String password);

}
