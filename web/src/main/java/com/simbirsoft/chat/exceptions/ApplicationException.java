package com.simbirsoft.chat.exceptions;

/**
 * Basic class for all application exceptions. It may be used to easy and convenient
 * handle of caught exceptions.
 */
public class ApplicationException extends Exception {
    public ApplicationException(String msg) {
        super(msg);
    }

    public ApplicationException(String msg, Throwable previous) {
        super(msg, previous);
    }
}
