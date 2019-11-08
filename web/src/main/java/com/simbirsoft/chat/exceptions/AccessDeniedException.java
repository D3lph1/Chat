package com.simbirsoft.chat.exceptions;

public class AccessDeniedException extends RuntimeApplicationException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
