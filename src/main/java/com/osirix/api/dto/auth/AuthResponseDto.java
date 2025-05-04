package com.osirix.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
	
	private String token;
	private String tokenType;

	public AuthResponseDto(String token) {
		this.token = token;
		this.tokenType = "BEARER ";
	}
	
}
