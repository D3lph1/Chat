package com.simbirsoft.chat.dto;

import com.simbirsoft.chat.entity.User;

public class DTOtoEntityConverter {
    // Private constructor because class contains only static methods.
    private DTOtoEntityConverter() {
    }

    public static User signup(SignupUserDTO userDTO, String hashedPassword) {
        return new User(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                hashedPassword
        );
    }
}
