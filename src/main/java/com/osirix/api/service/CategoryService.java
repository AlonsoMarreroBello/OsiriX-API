package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.category.CategoryRequestDto;
import com.osirix.api.dto.category.CategoryResponseDto;

public interface CategoryService {
	
	List<CategoryResponseDto> getAll();
	CategoryResponseDto getById(Long categoryId);
	
	CategoryResponseDto create(CategoryRequestDto request);
	
	CategoryResponseDto update(Long categoryId, CategoryRequestDto request);
	
	void deleteById(Long categoryId);

}
