package com.osirix.api.dto.request.publication;

import com.osirix.api.dto.request.RequestSimpleDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PublicationRequestSimpleDto extends RequestSimpleDto {

	private String developerName;
	private Long appId;
	
}
