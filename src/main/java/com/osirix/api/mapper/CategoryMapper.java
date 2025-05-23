package com.osirix.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.osirix.api.dto.category.CategoryRequestDto;
import com.osirix.api.dto.category.CategoryResponseDto;
import com.osirix.api.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

	@Mapping(target = "categoryId", ignore = true)
	Category toEntity(CategoryRequestDto request);
	
	CategoryResponseDto toResponse(Category category);
	
}
