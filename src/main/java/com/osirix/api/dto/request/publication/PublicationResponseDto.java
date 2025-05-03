package com.osirix.api.dto.request.publication;

import com.osirix.api.dto.app.AppResponseDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;
import com.osirix.api.dto.request.RequestResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PublicationResponseDto extends RequestResponseDto {

	private DeveloperResponseDto developer;
	private Long asignedStaffId;
	private AppResponseDto app;
	
}
