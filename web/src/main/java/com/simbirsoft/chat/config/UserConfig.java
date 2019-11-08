package com.simbirsoft.chat.config;

import com.simbirsoft.chat.service.user.UserInitializer;
import com.simbirsoft.chat.service.user.avatar.*;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.IdenticonAvatar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class UserConfig {
    public static final String AVATAR_PATH = "avatars";

    @Bean
    public Avatar avatarGenerator() {
        return IdenticonAvatar.newAvatarBuilder().build();
    }

    @Bean
    public AvatarPathNamingStrategy avatarPathNamingStrategy() {
        return new EmailAvatarPathNamingStrategy(Paths.get(AVATAR_PATH));
    }

    @Bean
    public CodeGenerationStrategy codeGenerationStrategy() {
        return new NameCodeGenerationStrategy();
    }


    @Bean
    public UserInitializer userInitializer(Avatar avatar, AvatarPathNamingStrategy namingStrategy) {
        return new CreateAvatarDecorator(
                avatar,
                new NameCodeGenerationStrategy(),
                namingStrategy
        );
    }
}
