package com.simbirsoft.chat.util;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.service.auth.UserContainer;
import org.springframework.security.core.Authentication;

public class UserUtil {
    /**
     * Private constructor to ensure non-instantiation because this class
     * contains only static methods.
     */
    private UserUtil() {
    }

    public static User retrieveUserFromAuth(Authentication auth) {
        UserContainer userContainer = (UserContainer) auth.getPrincipal();
        if (userContainer == null) {
            throw new IllegalArgumentException("Authentication can not be null");
        }

        return userContainer.getUser();
    }
}
