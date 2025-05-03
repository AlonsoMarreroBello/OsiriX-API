package com.osirix.api.dto.user.publisher;

import com.osirix.api.dto.user.UserRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PublisherRequestDto extends UserRequestDto {

	private String nif;
	private String publisherName;
	private String address;
	private Long assignedAdminId;

}
