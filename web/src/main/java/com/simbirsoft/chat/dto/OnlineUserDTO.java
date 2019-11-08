package com.simbirsoft.chat.dto;

public class OnlineUserDTO implements Comparable<OnlineUserDTO> {
    private final String firstName;

    private final String lastName;

    private final String email;

    private final String avatarUrl;

    public OnlineUserDTO(String firstName, String lastName, String email, String avatarUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int compareTo(OnlineUserDTO another) {
        return email.compareTo(another.getEmail());
    }
}
