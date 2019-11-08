package com.simbirsoft.chat.exceptions;

import com.simbirsoft.chat.service.http.Response;
import com.simbirsoft.chat.service.localization.LocalizationKeyResolver;
import com.simbirsoft.chat.util.MessageUtil;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collection;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger;

    private final LocalizationKeyResolver localizationKeyResolver;

    private final MessageSource messageSource;

    public RestExceptionHandler(Logger logger, LocalizationKeyResolver localizationKeyResolver, MessageSource messageSource) {
        this.logger = logger;
        this.localizationKeyResolver = localizationKeyResolver;
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Collection<String> messages = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {
            String message = localizationKeyResolver.resolve(error);
            if (message != null) {
                messages.add(message);
            }
        });

        return new ResponseEntity<>(new Response(Response.STATUS_VALIDATION_FAILED, messages), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAnother(Exception ex, WebRequest request) {
        logger.error("", ex);
        String error = MessageUtil.retrieveFromSource("error.internal_server_error", messageSource);

        return new ResponseEntity<>(
                new Response(Response.STATUS_INTERNAL_SERVER_ERROR).addError(error), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
