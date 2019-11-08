package com.simbirsoft.chat.service.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Class provides extracting of token parameter from request and further
 * authentication attempting.
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String PARAMETER = "token";

    public TokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        String param = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElse(request.getParameter(PARAMETER));

        String token = Optional.ofNullable(param)
                .map(String::trim)
                .orElseThrow(() -> new BadCredentialsException("Missing authentication token"));

        Authentication auth = new UsernamePasswordAuthenticationToken(token, token);

        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
