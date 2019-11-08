package com.simbirsoft.chat.config;

import com.simbirsoft.chat.service.localization.LocalizationKeyResolver;
import com.simbirsoft.chat.service.localization.MessageSourceLocalizationKeyResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class LocalizationConfig implements WebMvcConfigurer {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");

        return messageSource;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/" + UserConfig.AVATAR_PATH + "/**")
                .addResourceLocations("file:" + UserConfig.AVATAR_PATH + "/");
    }

    @Bean
    public LocalizationKeyResolver localizationKeyResolver() {
        return new MessageSourceLocalizationKeyResolver(messageSource());
    }
}
