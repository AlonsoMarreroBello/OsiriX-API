package com.osirix.api.config; 

import com.osirix.api.entity.User; 
import com.osirix.api.security.CustomUserDetails;
import com.osirix.api.service.impl.AppServiceImpl;
import com.osirix.api.utils.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("appSecurity")
public class AppSecurityExpressions {

    @Autowired
    private AppServiceImpl appService;

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

    
    public boolean isStaff(Authentication authentication) {
        User authenticatedUser = getAuthenticatedUser(authentication);
        if (authenticatedUser == null) {
            return false;
        }
        boolean isOfTypeStaff = authenticatedUser.getType() == UserType.STAFF;
        return isOfTypeStaff;
    }

    
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
            return false;
        }

        boolean isOwner = appService.isUserPublisherOfApp(publisherId, appIdLong);
        return isOwner;
    }

    
    public boolean canManageAppAssets(Authentication authentication, String appIdString) {
        User authenticatedUser = getAuthenticatedUser(authentication);
        if (authenticatedUser == null) {
            return false; // No autenticado o principal desconocido
        }

        if (authenticatedUser.getType() == UserType.STAFF) {
            return true;
        }

        if (authenticatedUser.getType() == UserType.PUBLISHER) {
            boolean isOwner = isAppOwnerPublisher(authentication, appIdString);
            return isOwner;
        }
        
        return false;
    }
}