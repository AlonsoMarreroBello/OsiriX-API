package com.osirix.api.dto.user.publisher;

import com.osirix.api.dto.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PublisherResponseDto extends UserResponseDto {
	
	private String nif;
	private String publisherName;
	private String address;

}
