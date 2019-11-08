package com.simbirsoft.chat.service.auth;

import com.simbirsoft.chat.entity.User;
import com.simbirsoft.chat.exceptions.UserNotFoundException;

import java.util.Optional;

/**
 * Defines interface that is used for token-based user authentication. It is
 * important that this abstraction implies stateless session.
 */
public interface UserTokenAuthenticationService {
    /**
     * Finds user with given email/password pair in the system, generates
     * unique authentication token, save it for user and return in string
     * representation. This string in future can be returned to user.
     * <p>
     * For better security, authentication token may own lifetime property
     * after which the token is invalidated.
     *
     * @param email    User email
     * @param password User password
     * @return Authentication token
     * @throws UserNotFoundException In case when user with passed email
     *                               and password not registered in the system.
     */
    String login(String email, String password) throws UserNotFoundException;

    /**
     * Finds and returns an user with passed authentication token if its
     * exists else returns {@link Optional#empty()}. In case of invalid
     * token this method must also return {@link Optional#empty()}.
     *
     * @param token
     * @return
     */
    Optional<User> findByToken(String token);
}
