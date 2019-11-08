package com.simbirsoft.chat.service.user.avatar;

import com.simbirsoft.chat.entity.User;

import java.nio.file.Path;

/**
 * The interface is responsible for building path to avatar of specified user.
 */
public interface AvatarPathNamingStrategy {
    Path path(User user);

    String relativeUrlString(User user);
}
