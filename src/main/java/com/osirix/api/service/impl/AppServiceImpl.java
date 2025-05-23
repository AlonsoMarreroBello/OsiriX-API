package com.osirix.api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.osirix.api.dto.app.AppRequestDto;
import com.osirix.api.dto.app.AppResponseDto;
import com.osirix.api.dto.category.CategoryRequestDto;
import com.osirix.api.entity.App;
import com.osirix.api.entity.Category;
import com.osirix.api.entity.Developer;
import com.osirix.api.entity.Publisher;
import com.osirix.api.entity.User;
import com.osirix.api.entity.UserLibrary;
import com.osirix.api.exception.ResourceNotFoundException;
import com.osirix.api.mapper.AppMapper;
import com.osirix.api.mapper.CategoryMapper;
import com.osirix.api.repository.AppRepository;
import com.osirix.api.repository.CategoryRepository;
import com.osirix.api.repository.DeveloperRepository;
import com.osirix.api.repository.PublisherRepository;
import com.osirix.api.repository.UserLibraryRepository;
import com.osirix.api.repository.UserRepository;
import com.osirix.api.service.AppService;

@Service
public class AppServiceImpl implements AppService {
	
	@Autowired
	AppRepository appRepository;
	
	@Autowired
	DeveloperRepository developerRepository;

	@Autowired
	PublisherRepository publisherRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserLibraryRepository userLibraryRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AppMapper appMapper;

	@Autowired
	CategoryMapper categoryMapper;

	@Override
	public List<AppResponseDto> getAll() {
		return appRepository.findAll().stream().map(appMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<AppResponseDto> getStack(Long appId) {
		return appRepository.findNext50(appId, PageRequest.of(0, 50))
								.stream().map(appMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public AppResponseDto getById(Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		return appMapper.toResponse(app);
	}

	@Override
	public List<AppResponseDto> getByPartialName(String name) {
		return appRepository.findByNameContaining(name)
								.stream().map(appMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<AppResponseDto> getAppsByPublisher(Long publisherId) {
		return appRepository.findByPublisherId(publisherId).stream().map(appMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<AppResponseDto> getAppsByDeveloper(Long developerId) {
		return appRepository.findByDeveloperId(developerId).stream().map(appMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<AppResponseDto> getAppsByCategory(List<Long> categoryIds) {
		return appRepository.findByCategoriesIds(categoryIds).stream().map(appMapper::toResponse).collect(Collectors.toList());
	}

	@Override
	public List<AppResponseDto> getAppsByUserId(Long userId) {
		List<UserLibrary> libraries = userLibraryRepository.findByUserId(userId);
		
		List<AppResponseDto> apps = new ArrayList<>();
		
		for (UserLibrary library : libraries) {
			apps.add(appMapper.toResponse(library.getApp()));
		}
		
		return apps;
		
	}
	
	@Override
	public AppResponseDto create(AppRequestDto request) {
		
		System.out.println(request);
		
		App app = appMapper.toEntity(request);
		
		Developer dev = developerRepository.findById(request.getDeveloperId())
											.orElseThrow(() -> new ResourceNotFoundException("developer not found"));
		
		Publisher publisher = publisherRepository.findById(request.getPublisherId())
											.orElseThrow(() -> new ResourceNotFoundException("developer not found"));
		app.setDeveloper(dev);
		app.setPublisher(publisher);
		app.setDownloads(0);
		app.setPublicationDate(LocalDate.now());
		
		Set<Category> categories = new HashSet<>();
		
		for (Category category : app.getCategories()) {
			Optional<Category> opCategory = categoryRepository.findBycategoryName(category.getCategoryName());
			
			if (opCategory.isEmpty()) {
				categories.add(categoryRepository.save(category));
			} else {
				categories.add(opCategory.get());
			}
			
		}
		
		app.setCategories(categories);
		
		return appMapper.toResponse(appRepository.save(app));
	}

	@Override
	public AppResponseDto update(Long appId, AppRequestDto request) {
		App app = appRepository.findById(appId)
								.orElseThrow(() -> new ResourceNotFoundException("App not found"));
		
		app.setDescription(request.getDescription());
		app.setVersion(request.getVersion());
		
		Set<Category> categories = new HashSet<>();
		for (CategoryRequestDto category : request.getCategories()) {
			Optional<Category> opCategory = categoryRepository.findBycategoryName(category.getCategoryName());
			
			if (opCategory.isEmpty()) {
				categories.add(categoryRepository.save(categoryMapper.toEntity(category)));
			} else {
				categories.add(opCategory.get());
			}
		}
		
		app.setCategories(categories);
		
		if (app.getDeveloper().getId() != request.getDeveloperId()) {
			Developer dev = developerRepository.findById(request.getDeveloperId())
					.orElseThrow(() -> new ResourceNotFoundException("developer not found"));
			app.setDeveloper(dev);
		}
		
		if (app.getPublisher().getId() != request.getPublisherId()) {
			Publisher publisher = publisherRepository.findById(request.getPublisherId())
					.orElseThrow(() -> new ResourceNotFoundException("developer not found"));
			app.setPublisher(publisher);
		}
		
		app.setEditDate(LocalDate.now());
		
		return appMapper.toResponse(appRepository.save(app));
	}

	@Override
	public AppResponseDto togglePublish(Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		
		app.setIsPublished(!app.getIsPublished());
		
		return appMapper.toResponse(appRepository.save(app));
	}

	@Override
	public AppResponseDto toggleShow(Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		
		app.setIsVisible(!app.getIsVisible());
		
		return appMapper.toResponse(appRepository.save(app));
	}

	@Override
	public AppResponseDto toggleDownload(Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		
		app.setIsDownloadable(!app.getIsDownloadable());
		
		return appMapper.toResponse(appRepository.save(app));
	}

	@Override
	public void deleteById(Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		
		appRepository.delete(app);	
	}

	@Override
	public boolean appExists(Long id) {
		Optional<App> app = appRepository.findById(id);
		return app.isPresent();
	}

	@Override
	public String getAppStoredFilename(Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		return  app.getAppId().toString();
	}

	@Override
	public boolean addAppToUserLibrary(Long userId, Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		Optional<UserLibrary> library_opt = userLibraryRepository.findByUserIdAndAppId(userId, appId);
		
		UserLibrary saved = null;
		
		if (library_opt.isPresent()) {
			UserLibrary userLibrary = UserLibrary.builder().app(app).user(user).build();
			
			saved = userLibraryRepository.save(userLibrary);
			
		}
		
		return saved != null;
		
	}

	@Override
	public boolean isUserPublisherOfApp(Long userId, Long appId) {
		App app = appRepository.findById(appId).orElseThrow(() -> new ResourceNotFoundException("App not found"));
		return app != null && app.getPublisher().getId().equals(userId);
	}

}
