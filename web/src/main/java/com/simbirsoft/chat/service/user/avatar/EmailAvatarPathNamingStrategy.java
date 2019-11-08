package com.simbirsoft.chat.service.user.avatar;

import com.simbirsoft.chat.entity.User;

import java.nio.file.Path;

public class EmailAvatarPathNamingStrategy implements AvatarPathNamingStrategy {
    private static final String EXTENSION = ".png";

    private final Path basePath;

    public EmailAvatarPathNamingStrategy(Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public Path path(User user) {
        return basePath.resolve(filename(user.getEmail()));
    }

    @Override
    public String relativeUrlString(User user) {
        return "/" + basePath + "/" + filename(user.getEmail());
    }

    private String filename(String email) {
        return email + EXTENSION;
    }
}
