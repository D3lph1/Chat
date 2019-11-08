package com.simbirsoft.chat.service.user.avatar;

import com.simbirsoft.chat.entity.User;

/**
 * Generates code by using user first and last name.
 */
public class NameCodeGenerationStrategy implements CodeGenerationStrategy {
    @Override
    public long generate(User user) {
        return (user.getFirstName() + user.getLastName()).hashCode();
    }
}
