package com.simbirsoft.chat.service.auth.oauth2;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    private String firstName;

    private String lastName;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        id = (String) attributes.get("sub");
        name = (String) attributes.get("name");
        email = (String) attributes.get("email");
        firstName = (String) attributes.get("given_name");
        lastName = (String) attributes.get("family_name");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
