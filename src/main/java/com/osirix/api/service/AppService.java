package com.osirix.api.service;

import java.util.List;

import com.osirix.api.dto.AppRequestDto;
import com.osirix.api.dto.AppResponseDto;

public interface AppService {

	List<AppResponseDto> getAll();
	List<AppResponseDto> getStack(Long appId);
	AppResponseDto geById(Long appId);
	List<AppResponseDto> getByPartialName(String name);
	
	AppResponseDto create(AppRequestDto request);
	
	AppResponseDto update(Long appId, AppRequestDto request);
	AppResponseDto togglePublish(Long appId);
	AppResponseDto toggleShow(Long appId);
	AppResponseDto toggleDownload(Long appId);
	
	Void deleteById(Long appId);
	
	boolean appExists(Long id);
	String getAppStoredFilename(Long appId);
	
}
