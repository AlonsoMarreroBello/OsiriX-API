package com.osirix.api.dto.user.publisher;

import com.osirix.api.dto.user.UserSimpleResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PublisherSimpleResponseDto extends UserSimpleResponseDto {

	private String publisherName;
}
