package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.app.AppRequestDto;
import com.osirix.api.dto.app.AppResponseDto;

public interface AppService {

	List<AppResponseDto> getAll();
	List<AppResponseDto> getStack(Long appId);
	AppResponseDto getById(Long appId);
	List<AppResponseDto> getByPartialName(String name);
	List<AppResponseDto> getAppsByPublisher(Long publisherId);
	List<AppResponseDto> getAppsByDeveloper(Long developerId);
	List<AppResponseDto> getAppsByCategory(List<Long> categoryId);
	
	List<AppResponseDto> getAppsByUserId(Long userId);
	
	AppResponseDto create(AppRequestDto request);
	
	AppResponseDto update(Long appId, AppRequestDto request);
	AppResponseDto togglePublish(Long appId);
	AppResponseDto toggleShow(Long appId);
	AppResponseDto toggleDownload(Long appId);
	
	void deleteById(Long appId);
	
	boolean appExists(Long id);
	String getAppStoredFilename(Long appId);
	
	boolean addAppToUserLibrary(Long userId, Long appId);
	boolean isUserPublisherOfApp(Long userId, Long appId);
	
}
