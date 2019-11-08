package com.simbirsoft.chat.service.user.avatar;

import com.simbirsoft.chat.entity.User;

/**
 * Consists logic for generation number that can be used for create personal user avatar.
 */
public interface CodeGenerationStrategy {
    long generate(User user);
}
