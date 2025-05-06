package com.osirix.api.dto.app;

import java.util.List;

import com.osirix.api.dto.category.CategoryResponseDto;
import com.osirix.api.dto.developer.DeveloperResponseDto;
import com.osirix.api.dto.user.publisher.PublisherResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponseDto {
	
	private Long appId;
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
