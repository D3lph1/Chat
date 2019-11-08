package com.simbirsoft.chat.service.localization;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.ObjectError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSourceLocalizationKeyResolver implements LocalizationKeyResolver {
    private final MessageSource messageSource;

    public MessageSourceLocalizationKeyResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String resolve(ObjectError error) {
        String rawMessage = error.getDefaultMessage();
        if (rawMessage == null) {
            return null;
        }

        Object[] arguments = error.getArguments();
        String message;

        // Find localization key in the message
        Pattern pattern = Pattern.compile("\\{(.+)}");
        Matcher matcher = pattern.matcher(rawMessage);
        if (matcher.find()) {
            String localizationKey = matcher.group(1);
            try {
                message = messageSource.getMessage(localizationKey, arguments, LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException e) {
                message = rawMessage;
            }
        } else {
            message = rawMessage;
        }

        return message;
    }
}
