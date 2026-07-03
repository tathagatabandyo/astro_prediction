package com.techtechnicworld.astroPrediction.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.techtechnicworld.astroPrediction.entity.User;
import com.techtechnicworld.astroPrediction.exception.ResourceNotFoundException;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }

        return userDetails;
    }

    public static User getCurrentUserEntity() {
        CustomUserDetails userDetails = SecurityUtils.getCurrentUserDetails();
        User user = userDetails.getUser();
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }
}
