package com.simbirsoft.chat.exceptions;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String msg) {
        super(msg);
    }

    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException(String.format("User with email \"%s\" not found.", email));
    }

    public static UserNotFoundException byToken(String token) {
        return new UserNotFoundException(String.format("User with token \"%s\" not found.", token));
    }
}
