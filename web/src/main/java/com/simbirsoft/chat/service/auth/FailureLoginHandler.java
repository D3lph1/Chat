package com.simbirsoft.chat.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.chat.service.http.Response;
import com.simbirsoft.chat.util.MessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class FailureLoginHandler implements AuthenticationFailureHandler {
    private final ObjectMapper jackson;

    private final MessageSource messageSource;

    public FailureLoginHandler(ObjectMapper jackson, MessageSource messageSource) {
        this.jackson = jackson;
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json");
        String error = MessageUtil.retrieveFromSource("auth.login.invalid_credentials", messageSource);
        jackson.writeValue(response.getWriter(), new Response(Response.STATUS_FAILED).addError(error));
        response.setStatus(HttpStatus.NOT_FOUND.value());
    }
}
