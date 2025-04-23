package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.CategoryRequestDto;
import com.osirix.api.dto.CategoryResponseDto;

public interface CategoryService {
	
	CategoryResponseDto getAll();
	List<CategoryResponseDto> getById(Long categoryId);
	
	CategoryResponseDto create(CategoryRequestDto request);
	
	CategoryResponseDto update(Long categoryId, CategoryRequestDto request);
	
	Void deleteById(Long categoryId);

}
