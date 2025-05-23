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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Operation(summary = "User Login", description = "Authenticates a user and returns an authentication token upon successful login.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User logged in successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid login request (e.g., missing fields)"),
			@ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(
			@Valid @RequestBody AuthLoginRequestDto request) {

		AuthResponseDto authResponse = userDetailsService.login(request);
		
		ApiResponseDto<AuthResponseDto> response = new ApiResponseDto<>("User logged in successfully", HttpStatus.OK.value(), authResponse);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@Operation(summary = "User Registration", description = "Registers a new user and returns an authentication token upon successful registration.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User registered successfully and logged in"),
			@ApiResponse(responseCode = "400", description = "Invalid registration request (e.g., missing fields, invalid email, password too short)"),
			@ApiResponse(responseCode = "409", description = "Conflict - User with the given email or username already exists"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("/register")
	public ResponseEntity<ApiResponseDto<AuthResponseDto>> register(
			@Valid @RequestBody UserRequestDto request) {
		
		AuthResponseDto authResponse = userDetailsService.register(request);
		
		ApiResponseDto<AuthResponseDto> response = new ApiResponseDto<>("User registered successfully", HttpStatus.OK.value(), authResponse);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	

}
