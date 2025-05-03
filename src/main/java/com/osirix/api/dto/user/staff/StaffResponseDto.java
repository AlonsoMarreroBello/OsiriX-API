package com.osirix.api.dto.user.staff;


import java.util.List;

import com.osirix.api.dto.user.UserResponseDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;

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
