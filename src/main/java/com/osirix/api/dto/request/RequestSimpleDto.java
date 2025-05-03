package com.osirix.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSimpleDto {
	
	private Long userId;
	private String requestTitle;
	private String requestBody;

}
