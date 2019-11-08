package com.simbirsoft.chat.service.user;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserInitializationException;

/**
 * The interface represents logic that will be executed after user registration successfully passed.
 */
public interface UserInitializer {
    void init(User user) throws UserInitializationException;
}
