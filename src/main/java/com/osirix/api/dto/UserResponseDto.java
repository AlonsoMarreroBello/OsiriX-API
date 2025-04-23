package com.osirix.api.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
	
	private Long id;
	private String username;
	private String email;
	private LocalDate registerDate;
	private LocalDate lastLogin;
	private Boolean isEnabled;
	private Boolean accountNotLocked;
	private String userType;

}
