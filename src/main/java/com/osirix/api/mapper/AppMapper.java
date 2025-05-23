package com.osirix.api.mapper;

import java.time.LocalDate;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.osirix.api.dto.app.AppRequestDto;
import com.osirix.api.dto.app.AppResponseDto;
import com.osirix.api.entity.App;

@Mapper(componentModel = "spring", uses = {PublisherMapper.class, CategoryMapper.class})
public interface AppMapper {
	
	@Mapping(target = "appId", ignore = true)
	@Mapping(target = "developer", ignore = true)
	@Mapping(target = "downloads", ignore = true)
	@Mapping(target = "editDate", ignore = true)
	@Mapping(target = "publicationDate", ignore = true)
	@Mapping(target = "publisher", ignore = true)
	App toEntity(AppRequestDto request);
	
	AppResponseDto toResponse(App app);
	
	@AfterMapping
	default void afterMapping(App app) {
		app.setEditDate(LocalDate.now());
	}
	
}
