package com.osirix.api.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class StaffResponseDto extends UserResponseDto {

	private List<PublisherResponseDto> assignedPublishers;
	
}
