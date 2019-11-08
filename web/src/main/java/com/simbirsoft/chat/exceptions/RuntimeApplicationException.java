package com.simbirsoft.chat.exceptions;

/**
 * Basic class for all runtime application exceptions. It may be used to easy and
 * convenient handle of caught exceptions.
 */
public class RuntimeApplicationException extends RuntimeException {
    public RuntimeApplicationException(String message) {
        super(message);
    }
}
