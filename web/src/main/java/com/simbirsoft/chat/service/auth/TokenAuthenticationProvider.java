package com.simbirsoft.chat.service.auth;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Retrieves user with given token from {@link UserTokenAuthenticationService} and builds
 * {@link UserDetails} object.
 */
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private UserTokenAuthenticationService auth;

    public TokenAuthenticationProvider(UserTokenAuthenticationService auth) {
        this.auth = auth;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        //
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        Object token = authentication.getCredentials();
        User user = Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(auth::findByToken)
                .orElseThrow(() -> new BadCredentialsException(
                        "User with given credentials not found",
                        UserNotFoundException.byToken(String.valueOf(token))
                ));

        return new UserContainer(user);
    }
}
