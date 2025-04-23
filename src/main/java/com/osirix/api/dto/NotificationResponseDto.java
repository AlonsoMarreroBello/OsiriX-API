package com.osirix.api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
	
	private Long id;
	private Long userId;
	private String message;
	private LocalDateTime sendDate;
	private boolean seen;

}
