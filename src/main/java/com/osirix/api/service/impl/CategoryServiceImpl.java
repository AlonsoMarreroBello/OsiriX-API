package com.osirix.api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.osirix.api.dto.category.CategoryRequestDto;
import com.osirix.api.dto.category.CategoryResponseDto;
import com.osirix.api.entity.Category;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.CategoryMapper;
import com.osirix.api.repository.CategoryRepository;
import com.osirix.api.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	CategoryMapper categoryMapper;
	
	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public List<CategoryResponseDto> getAll() {
		return categoryRepository.findAll().stream().map(categoryMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public CategoryResponseDto getById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		
		return categoryMapper.toResponse(category);
	}

	@Override
	public CategoryResponseDto create(CategoryRequestDto request) {
		
		Category category = categoryMapper.toEntity(request);
		
		return categoryMapper.toResponse(categoryRepository.save(category));
	}

	@Override
	public CategoryResponseDto update(Long categoryId, CategoryRequestDto request) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		
		category.setCategoryName(request.getCategoryName());
		
		Category updatedCategory = categoryRepository.save(category);
		return categoryMapper.toResponse(updatedCategory);
	}

	@Override
	public void deleteById(Long categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
		
		categoryRepository.delete(category);
	}

}
