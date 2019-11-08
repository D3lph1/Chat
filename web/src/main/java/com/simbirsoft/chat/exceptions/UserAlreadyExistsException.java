package com.simbirsoft.chat.exceptions;

public class UserAlreadyExistsException extends ApplicationException {
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }

    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException(String.format("User with email \"%s\" already exists.", email));
    }
}
