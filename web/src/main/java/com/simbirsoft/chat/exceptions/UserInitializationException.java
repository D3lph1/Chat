package com.simbirsoft.chat.exceptions;

public class UserInitializationException extends ApplicationException {
    public UserInitializationException(Throwable previous) {
        super("An exception occurred during user initialization", previous);
    }
}
