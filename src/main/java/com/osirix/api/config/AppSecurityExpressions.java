package com.osirix.api.config;

import com.osirix.api.entity.User;
import com.osirix.api.security.CustomUserDetails;
import com.osirix.api.service.AppService;
import com.osirix.api.utils.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Provides custom security expressions for use with Spring Security's expression-based access control.
 * This class allows for more complex authorization logic to be defined and referenced in security annotations
 * (e.g., {@code @PreAuthorize}, {@code @PostAuthorize}).
 *
 * @author Alonso Marrero Bello
 */
@Component("appSecurity")
public class AppSecurityExpressions {

    /**
     * Service for accessing application-related data and business logic.
     * Used here to verify application ownership.
     */
    @Autowired
    private AppService appService; 
    
    
    /**
     * Retrieves the custom {@link User} entity from the Spring Security {@link Authentication} object.
     *
     * @param authentication The current authentication object.
     * @return The {@link User} object if the principal is a {@link CustomUserDetails} instance and authenticated,
     *         otherwise {@code null}.
     */
    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        return null;
    }


    /**
     * Checks if the currently authenticated user is a staff member.
     *
     * @param authentication The current Spring Security {@link Authentication} object.
     * @return {@code true} if the authenticated user is of type {@link UserType#STAFF}, {@code false} otherwise.
     */
    public boolean isStaff(Authentication authentication) {
        User authenticatedUser = getAuthenticatedUser(authentication);
        if (authenticatedUser == null) {
            return false;
        }
        // Direct comparison for enum types is safe and clear.
        return authenticatedUser.getType() == UserType.STAFF;
    }


    /**
     * Checks if the currently authenticated user is a publisher and is the owner of the specified application.
     *
     * @param authentication The current Spring Security {@link Authentication} object.
     * @param appIdString    The ID of the application as a String.
     * @return {@code true} if the authenticated user is a {@link UserType#PUBLISHER} and owns the app,
     *         {@code false} otherwise (including if {@code appIdString} is not a valid Long).
     */
    public boolean isAppOwnerPublisher(Authentication authentication, String appIdString) {
        User authenticatedUser = getAuthenticatedUser(authentication);
        if (authenticatedUser == null || authenticatedUser.getType() != UserType.PUBLISHER) {
            return false;
        }

        Long publisherId = authenticatedUser.getId();
        Long appIdLong;
        try {
            appIdLong = Long.parseLong(appIdString);
        } catch (NumberFormatException e) {
            // If appIdString is not a valid number, the user cannot be the owner.
            return false;
        }

        return appService.isUserPublisherOfApp(publisherId, appIdLong);
    }


    /**
     * Checks if the currently authenticated user has permission to manage assets for a specific application.
     * Staff members can manage assets for any app. Publishers can manage assets only for apps they own.
     *
     * @param authentication The current Spring Security {@link Authentication} object.
     * @param appIdString    The ID of the application as a String, whose assets are being considered.
     * @return {@code true} if the user is authorized to manage the app's assets (either staff or owner publisher),
     *         {@code false} otherwise.
     */
    public boolean canManageAppAssets(Authentication authentication, String appIdString) {
        User authenticatedUser = getAuthenticatedUser(authentication);
        if (authenticatedUser == null) {
            return false;
        }

        if (authenticatedUser.getType() == UserType.STAFF) {
            return true; // Staff members can manage any app's assets.
        }

        if (authenticatedUser.getType() == UserType.PUBLISHER) {
            // Publishers can manage assets if they are the owner of the app.
            return isAppOwnerPublisher(authentication, appIdString);
        }

        // Users of other types (or unauthenticated) cannot manage app assets by default.
        return false;
    }
}