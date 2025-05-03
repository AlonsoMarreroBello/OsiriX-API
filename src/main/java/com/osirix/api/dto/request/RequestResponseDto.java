package com.osirix.api.dto.request;

import java.time.LocalDate;

import com.osirix.api.dto.user.publisher.PublisherSimpleResponseDto;
import com.osirix.api.entity.RequestStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestResponseDto {
	
	private Long id;
	private PublisherSimpleResponseDto user;
	private LocalDate requestDate;
	private RequestStatus requestStatus;
	private String adminComments;
	private String requestTitle;
	private String requestBody;

}
