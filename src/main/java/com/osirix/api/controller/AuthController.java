package com.osirix.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.auth.AuthLoginRequestDto;
import com.osirix.api.dto.auth.AuthResponseDto;
import com.osirix.api.dto.user.UserRequestDto;
import com.osirix.api.service.impl.UserDetailsServiceImpl;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@RequestBody AuthLoginRequestDto request) {

		AuthResponseDto authResponse = userDetailsService.login(request);
		
		ApiResponseDto<AuthResponseDto> response = new ApiResponseDto<>("User loged succesfully", 200, authResponse);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponseDto<AuthResponseDto>> register(@RequestBody UserRequestDto request) {
		
		AuthResponseDto authResponse = userDetailsService.register(request);
		
		ApiResponseDto<AuthResponseDto> response = new ApiResponseDto<>("User loged succesfully", 200, authResponse);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	

}
