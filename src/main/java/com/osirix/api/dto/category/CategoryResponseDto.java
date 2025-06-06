package com.osirix.api.dto.category;

import com.osirix.api.entity.CategoryType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
	
	private Long categoryId;
	private String categoryName;
	private CategoryType categoryType;
}
