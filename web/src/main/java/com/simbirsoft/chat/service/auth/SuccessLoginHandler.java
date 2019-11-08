package com.simbirsoft.chat.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simbirsoft.chat.service.http.Response;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class SuccessLoginHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper jackson;

    public SuccessLoginHandler(ObjectMapper jackson) {
        this.jackson = jackson;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        response.setContentType("application/json");
        jackson.writeValue(response.getWriter(), new Response(Response.STATUS_SUCCESS));
    }
}
