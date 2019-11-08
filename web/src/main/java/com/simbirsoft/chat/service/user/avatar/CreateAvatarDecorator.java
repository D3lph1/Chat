package com.simbirsoft.chat.service.user.avatar;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserInitializationException;
import com.simbirsoft.chat.service.user.UserInitializer;
import com.talanlabs.avatargenerator.Avatar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This {@link UserInitializer} generates avatar for new user.
 */
public class CreateAvatarDecorator implements UserInitializer {
    private final Avatar avatarGenerator;

    private final CodeGenerationStrategy codeGenerationStrategy;

    private final AvatarPathNamingStrategy pathNamingStrategy;

    private final UserInitializer userInitializer;

    public CreateAvatarDecorator(
            Avatar avatarGenerator,
            CodeGenerationStrategy codeGenerationStrategy,
            AvatarPathNamingStrategy pathNamingStrategy
    ) {
        this(avatarGenerator, codeGenerationStrategy, pathNamingStrategy, null);
    }

    public CreateAvatarDecorator(
            Avatar avatarGenerator,
            CodeGenerationStrategy codeGenerationStrategy,
            AvatarPathNamingStrategy pathNamingStrategy,
            UserInitializer userInitializer
    ) {
        this.avatarGenerator = avatarGenerator;
        this.codeGenerationStrategy = codeGenerationStrategy;
        this.pathNamingStrategy = pathNamingStrategy;
        this.userInitializer = userInitializer;
    }

    @Override
    public void init(User user) throws UserInitializationException {
        if (userInitializer != null) {
            userInitializer.init(user);
        }

        Path avatarPath = pathNamingStrategy.path(user);
        File avatarFile = avatarPath.toFile();
        byte[] bytes = avatarGenerator.createAsPngBytes(codeGenerationStrategy.generate(user));
        try {
            if (!avatarFile.exists()) {
                avatarFile.getParentFile().mkdirs();
            }
            Files.write(avatarPath, bytes);
        } catch (IOException e) {
            throw new UserInitializationException(e);
        }
    }
}
