package com.osirix.api.dto.user.staff;

import java.util.List;

import com.osirix.api.dto.user.UserRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class StaffRequestDto extends UserRequestDto {

	List<Long> AsignedPublishersIds;
	List<String> roles;
	
}
