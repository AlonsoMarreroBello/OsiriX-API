package com.osirix.api.controller;

import com.osirix.api.dto.ApiResponseDto;
import com.osirix.api.dto.app.AppRequestDto;
import com.osirix.api.dto.app.AppResponseDto;
import com.osirix.api.service.impl.AppServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "App Management", description = "APIs for managing applications")
public class AppController {

    private static final String APP_RESOURCE = "/apps";
    private static final String APP_ID_PATH = APP_RESOURCE + "/{appId}";
    private static final String APP_STACK_PATH = APP_RESOURCE + "/stack/{appId}";
    private static final String APP_SEARCH_PATH = APP_RESOURCE + "/search";
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

    @Operation(summary = "Ping App Controller", description = "A simple ping endpoint to check if the App controller is responsive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pong response indicating the service is up.")
    })
    @GetMapping(value = APP_RESOURCE + "/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(HttpStatus.OK).body("pong apps....");
    }

    @Operation(summary = "Get all apps", description = "Retrieves a list of all available applications.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of apps"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_RESOURCE)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAllApps() {
        List<AppResponseDto> apps = appService.getAll();
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get app by ID", description = "Retrieves a specific application by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved app"),
            @ApiResponse(responseCode = "404", description = "App not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_ID_PATH)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> getAppById(
            @Parameter(description = "ID of the app to retrieve", required = true, example = "1") @PathVariable Long appId) {
        AppResponseDto app = appService.getById(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App fetched successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a new app", description = "Creates a new application with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "App created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for app creation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(APP_RESOURCE)
    public ResponseEntity<ApiResponseDto<AppResponseDto>> createApp(
            @Valid @org.springframework.web.bind.annotation.RequestBody AppRequestDto appRequestDto) {
        AppResponseDto createdApp = appService.create(appRequestDto);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App created successfully",
                HttpStatus.CREATED.value(),
                createdApp);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing app", description = "Updates an existing application identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data for app update"),
            @ApiResponse(responseCode = "404", description = "App not found with the given ID to update"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping(APP_ID_PATH)
    @PreAuthorize("@appSecurity.canManageAppAssets(authentication, #appId)")
    public ResponseEntity<ApiResponseDto<AppResponseDto>> updateApp(
            @Parameter(description = "ID of the app to update", required = true, example = "1") @PathVariable Long appId,
            @Valid @org.springframework.web.bind.annotation.RequestBody AppRequestDto appRequestDto) {
        AppResponseDto updatedApp = appService.update(appId, appRequestDto);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App updated successfully",
                HttpStatus.OK.value(),
                updatedApp);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete an app", description = "Deletes an application by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "App deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "App not found with the given ID to delete"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(APP_ID_PATH)
    @PreAuthorize("@appSecurity.canManageAppAssets(authentication, #appId)")
    public ResponseEntity<ApiResponseDto<Void>> deleteApp(
            @Parameter(description = "ID of the app to delete", required = true, example = "1") @PathVariable Long appId) {
        appService.deleteById(appId);
        ApiResponseDto<Void> response = new ApiResponseDto<>(
                "App deleted successfully",
                HttpStatus.NO_CONTENT.value(),
                null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @Operation(summary = "Get app stack", description = "Retrieves the stack (e.g., related versions or components) for a specific app.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App stack fetched successfully"),
            @ApiResponse(responseCode = "404", description = "App not found or no stack information available"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_STACK_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppStack(
            @Parameter(description = "ID of the app to get the stack for", required = true, example = "1") @PathVariable Long appId) {
        List<AppResponseDto> appsStack = appService.getStack(appId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "App stack fetched successfully",
                HttpStatus.OK.value(),
                appsStack);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Search apps by partial name", description = "Retrieves a list of applications whose names partially match the provided query string.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apps fetched by partial name successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search query provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_SEARCH_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByPartialName(
            @Parameter(description = "Partial name to search for within app names", required = true, example = "Game") @RequestParam String name) {
        List<AppResponseDto> apps = appService.getByPartialName(name);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by partial name successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get apps by publisher ID", description = "Retrieves all applications published by a specific publisher.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apps fetched by publisher successfully"),
            @ApiResponse(responseCode = "404", description = "Publisher not found or no apps associated with this publisher ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_BY_PUBLISHER_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByPublisherId(
            @Parameter(description = "ID of the publisher", required = true, example = "101") @PathVariable Long publisherId) {
        List<AppResponseDto> apps = appService.getAppsByPublisher(publisherId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by publisher successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get apps by developer ID", description = "Retrieves all applications developed by a specific developer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apps fetched by developer successfully"),
            @ApiResponse(responseCode = "404", description = "Developer not found or no apps associated with this developer ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_BY_DEVELOPER_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByDeveloperId(
            @Parameter(description = "ID of the developer", required = true, example = "202") @PathVariable Long developerId) {
        List<AppResponseDto> apps = appService.getAppsByDeveloper(developerId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by developer successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get apps by category IDs", description = "Retrieves applications that belong to one or more specified categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apps fetched by categories successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category IDs provided (e.g., empty list or non-numeric values)"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_BY_CATEGORIES_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByCategoryIds(
            @Parameter(description = "List of category IDs to filter apps by. Example: 1,2,3", required = true, example = "1,2") @RequestParam List<Long> categoryIds) {
        List<AppResponseDto> apps = appService.getAppsByCategory(categoryIds);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched by categories successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get apps by user ID (user's library)", description = "Retrieves all applications present in a specific user's library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Apps fetched for user successfully"),
            @ApiResponse(responseCode = "404", description = "User not found or the user has no apps in their library"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_BY_USER_PATH)
    public ResponseEntity<ApiResponseDto<List<AppResponseDto>>> getAppsByUserId(
            @Parameter(description = "ID of the user whose app library is to be retrieved", required = true, example = "303") @PathVariable Long userId) {
        List<AppResponseDto> apps = appService.getAppsByUserId(userId);
        ApiResponseDto<List<AppResponseDto>> response = new ApiResponseDto<>(
                "Apps fetched for user successfully",
                HttpStatus.OK.value(),
                apps);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Toggle app publish status", description = "Toggles the publish status (e.g., published/unpublished) of an application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App publish status updated successfully"),
            @ApiResponse(responseCode = "404", description = "App not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error during status toggle")
    })
    @PatchMapping(APP_TOGGLE_PUBLISH_PATH)
    @PreAuthorize("@appSecurity.canManageAppAssets(authentication, #appId)")
    public ResponseEntity<ApiResponseDto<AppResponseDto>> toggleAppPublishStatus(
            @Parameter(description = "ID of the app whose publish status is to be toggled", required = true, example = "1") @PathVariable Long appId) {
        AppResponseDto app = appService.togglePublish(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App publish status updated successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Toggle app visibility", description = "Toggles the visibility status (e.g., shown/hidden in listings) of an application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App visibility status updated successfully"),
            @ApiResponse(responseCode = "404", description = "App not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error during visibility toggle")
    })
    @PatchMapping(APP_TOGGLE_VISIBILITY_PATH)
    @PreAuthorize("@appSecurity.canManageAppAssets(authentication, #appId)")
    public ResponseEntity<ApiResponseDto<AppResponseDto>> toggleAppVisibility(
            @Parameter(description = "ID of the app whose visibility is to be toggled", required = true, example = "1") @PathVariable Long appId) {
        AppResponseDto app = appService.toggleShow(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App visibility status updated successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Toggle app downloadable status", description = "Toggles whether an application can be downloaded by users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App downloadable status updated successfully"),
            @ApiResponse(responseCode = "404", description = "App not found with the given ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error during downloadable status toggle")
    })
    @PatchMapping(APP_TOGGLE_DOWNLOADABLE_PATH)
    @PreAuthorize("@appSecurity.canManageAppAssets(authentication, #appId)")
    public ResponseEntity<ApiResponseDto<AppResponseDto>> toggleAppDownloadableStatus(
            @Parameter(description = "ID of the app whose downloadable status is to be toggled", required = true, example = "1") @PathVariable Long appId) {
        AppResponseDto app = appService.toggleDownload(appId);
        ApiResponseDto<AppResponseDto> response = new ApiResponseDto<>(
                "App downloadable status updated successfully",
                HttpStatus.OK.value(),
                app);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get app stored filename", description = "Retrieves the stored filename for the application's binary or package.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "App filename fetched successfully"),
            @ApiResponse(responseCode = "404", description = "App not found or filename is not available for this app"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(APP_FILENAME_PATH)
    public ResponseEntity<ApiResponseDto<String>> getAppFilename(
            @Parameter(description = "ID of the app to get the filename for", required = true, example = "1") @PathVariable Long appId) {
        String filename = appService.getAppStoredFilename(appId);
        ApiResponseDto<String> response = new ApiResponseDto<>(
                "App filename fetched successfully",
                HttpStatus.OK.value(),
                filename);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Add app to user library", description = "Adds a specified application to a specified user's personal library.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation status for adding app to user library"),
            @ApiResponse(responseCode = "404", description = "App or User not found with the given IDs"),
            @ApiResponse(responseCode = "409", description = "Conflict, e.g., app already in library"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(APP_ADD_TO_LIBRARY_PATH)
    public ResponseEntity<ApiResponseDto<Boolean>> addAppToUserLibrary(
            @Parameter(description = "ID of the app to add to the library", required = true, example = "1") @PathVariable Long appId,
            @Parameter(description = "ID of the user whose library the app will be added to", required = true, example = "303") @PathVariable Long userId) {
        boolean success = appService.addAppToUserLibrary(userId, appId);
        ApiResponseDto<Boolean> response = new ApiResponseDto<>(
                success ? "App added to user library successfully" : "Failed to add app to user library or app already present",
                HttpStatus.OK.value(),
                success);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}