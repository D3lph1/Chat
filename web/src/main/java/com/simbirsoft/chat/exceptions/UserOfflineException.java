package com.simbirsoft.chat.exceptions;

public class UserOfflineException extends ApplicationException {
    public UserOfflineException(String msg) {
        super(msg);
    }

    public static UserOfflineException byEmail(String email) {
        return new UserOfflineException(String.format(
                "User with email \"%s\" is currently offline so action can not be performed"
                , email
        ));
    }
}
