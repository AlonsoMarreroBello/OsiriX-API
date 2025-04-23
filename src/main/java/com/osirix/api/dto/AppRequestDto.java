package com.osirix.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppRequestDto {
	
	private Long publisherId;
	private Long developerId;
	private String name;
	private String version;
	private Boolean isPublished;
	private Boolean isVisible;  
    private Boolean isDownloadable;
    private List<CategoryRequestDto> categories;
    private String description;

}
