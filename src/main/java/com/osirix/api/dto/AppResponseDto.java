package com.osirix.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponseDto {
	
	private Long id;
	private PublisherResponseDto publisher;
	private DeveloperResponseDto developer;
	private String name;
	private String version;
	private Boolean isPublished;
	private Boolean isVisible;  
    private Boolean isDownloadable;
    private List<CategoryResponseDto> categories;
    private String description;
    private Integer downloads;

}
