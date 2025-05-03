package com.osirix.api.dto.request.publication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PublicationRequestDto extends PublicationRequestSimpleDto {

	private String status;
	private String adminComments;
	
}
