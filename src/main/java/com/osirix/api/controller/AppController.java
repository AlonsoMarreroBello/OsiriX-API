package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.app.AppRequestDto;
import com.osirix.api.dto.app.AppResponseDto;
import com.osirix.api.service.impl.AppServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1") 
public class AppController {

    private static final String APP_RESOURCE = "/apps";
    private static final String APP_ID_PATH = APP_RESOURCE + "/{appId}";
    private static final String APP_STACK_PATH = APP_RESOURCE + "/stack/{appId}";
    private static final String APP_SEARCH_PATH = APP_RESOURCE + "/search"; // Por nombre parcial
    private static final String APP_BY_PUBLISHER_PATH = APP_RESOURCE + "/by-publisher/{publisherId}";
    private static final String APP_BY_DEVELOPER_PATH = APP_RESOURCE + "/by-developer/{developerId}";
    private static final String APP_BY_CATEGORIES_PATH = APP_RESOURCE + "/by-categories";
    private static final String APP_BY_USER_PATH = APP_RESOURCE + "/by-user/{userId}";
    private static final String APP_TOGGLE_PUBLISH_PATH = APP_ID_PATH + "/toggle-publish";
    private static final String APP_TOGGLE_VISIBILITY_PATH = APP_ID_PATH + "/toggle-visibility";
    private static final String APP_TOGGLE_DOWNLOADABLE_PATH = APP_ID_PATH + "/toggle-downloadable";
    private static final String APP_FILENAME_PATH = APP_ID_PATH + "/filename";
    private static final String APP_ADD_TO_LIBRARY_PATH = APP_ID_PATH + "/library/user/{userId}";


    @Autowired
    AppServiceImpl appService;

    @GetMapping(value = APP_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong apps....");
    }

    @GetMapping(APP_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAllApps() {
        List<AppResponseDto> apps = appService.getAll();
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(APP_ID_PATH)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> getAppById(@PathVariable Long appId) {
        AppResponseDto app = appService.getById(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App fetched successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(APP_RESOURCE)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> createApp(@RequestBody AppRequestDto appRequestDto) {
        AppResponseDto createdApp = appService.create(appRequestDto);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App created successfully",
                HttpStatus.CREATED.value(),
                createdApp);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(APP_ID_PATH)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> updateApp(@PathVariable Long appId, @RequestBody AppRequestDto appRequestDto) {
        AppResponseDto updatedApp = appService.update(appId, appRequestDto);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App updated successfully",
                HttpStatus.OK.value(),
                updatedApp);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(APP_ID_PATH)
    public ResponseEntity<ApiResponseDto<Void>> deleteApp(@PathVariable Long appId) {
        appService.deleteById(appId);
        ApiResponseDto<Void> response = new ApiResponseDto<>(
                "App deleted successfully",
                HttpStatus.NO_CONTENT.value(),
                null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    // --- Métodos Adicionales ---

    @GetMapping(APP_STACK_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppStack(@PathVariable Long appId) {
        List<AppResponseDto> appsStack = appService.getStack(appId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "App stack fetched successfully",
                HttpStatus.OK.value(),
                appsStack);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(APP_SEARCH_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByPartialName(@RequestParam String name) {
        List<AppResponseDto> apps = appService.getByPartialName(name);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by partial name successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(APP_BY_PUBLISHER_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByPublisherId(@PathVariable Long publisherId) {
        List<AppResponseDto> apps = appService.getAppsByPublisher(publisherId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by publisher successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(APP_BY_DEVELOPER_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByDeveloperId(@PathVariable Long developerId) {
        List<AppResponseDto> apps = appService.getAppsByDeveloper(developerId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by developer successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(APP_BY_CATEGORIES_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByCategoryIds(@RequestParam List<Long> categoryIds) {
        List<AppResponseDto> apps = appService.getAppsByCategory(categoryIds);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by categories successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(APP_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByUserId(@PathVariable Long userId) {
        List<AppResponseDto> apps = appService.getAppsByUserId(userId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched for user successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(APP_TOGGLE_PUBLISH_PATH)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> toggleAppPublishStatus(@PathVariable Long appId) {
        AppResponseDto app = appService.togglePublish(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App publish status updated successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(APP_TOGGLE_VISIBILITY_PATH)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> toggleAppVisibility(@PathVariable Long appId) {
        AppResponseDto app = appService.toggleShow(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App visibility status updated successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping(APP_TOGGLE_DOWNLOADABLE_PATH)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> toggleAppDownloadableStatus(@PathVariable Long appId) {
        AppResponseDto app = appService.toggleDownload(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App downloadable status updated successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping(APP_FILENAME_PATH)
    public ResponseEntity<ApiResponseDto<String>> getAppFilename(@PathVariable Long appId) {
        String filename = appService.getAppStoredFilename(appId);
        ApiResponseDto<String> response = new ApiResponseDto<>(
                "App filename fetched successfully",
                HttpStatus.OK.value(),
                filename);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(APP_ADD_TO_LIBRARY_PATH)
    public ResponseEntity<ApiResponseDto<Boolean>> addAppToUserLibrary(@PathVariable Long appId, @PathVariable Long userId) {
        boolean success = appService.addAppToUserLibrary(userId, appId); // Asegúrate que el orden de parámetros coincida con el servicio
        ApiResponseDto<Boolean> response = new ApiResponseDto<>(
                success ? "App added to user library successfully" : "Failed to add app to user library",
                HttpStatus.OK.value(), // O CREATED si es más apropiado y la operación es idempotente
                success);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}