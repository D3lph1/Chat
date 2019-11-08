package com.simbirsoft.chat.service.localization;

import org.springframework.validation.ObjectError;

public interface LocalizationKeyResolver {
    String resolve(ObjectError error);
}
