package com.osirix.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {

	private LocalDateTime timestamp;
	private String message;
	private int code;
	private T data;
	
	public ApiResponseDto(String message, int code, T data) {
		this.timestamp = LocalDateTime.now();
		this.message = message;
		this.code = code;
		this.data = data;
	}
	
}
