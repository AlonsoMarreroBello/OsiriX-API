package com.osirix.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequestDto {
	
	@NotBlank
	private String email;
	
	@NotEmpty(message = "password cant be empty")
	private String password;
	
	@NotBlank
	private String origin;

}
