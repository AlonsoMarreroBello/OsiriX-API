package com.osirix.api.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDto {
	
	private Long id;
	private String RoleName;
	private String RoleDescription;

}
